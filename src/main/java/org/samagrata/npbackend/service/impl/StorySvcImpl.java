package org.samagrata.npbackend.service.impl;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

import org.samagrata.npbackend.dto.StoryRequest;
import org.samagrata.npbackend.dto.StoryResponse;
import org.samagrata.npbackend.entity.CaseEntity;
import org.samagrata.npbackend.entity.StoryEntity;
import org.samagrata.npbackend.entity.CommentEntity;
import org.samagrata.npbackend.entity.ExpenseEntity;
import org.samagrata.npbackend.entity.FundEntity;
import org.samagrata.npbackend.entity.ReceivableEntity;
import org.samagrata.npbackend.exception.ResourceNotFoundException;
import org.samagrata.npbackend.repository.*;
import org.samagrata.npbackend.service.StoryService;

@Slf4j
@Service
@Transactional
public class StorySvcImpl implements StoryService {
  
  @Autowired
  private StoryRepository storyRepo;

  @Autowired
  private CaseRepository caseRepo;

  @Autowired
  private CommentRepository commentRepo;

  @Autowired
  private FundRepository fundRepo;

  @Autowired
  private ReceivableRepository receivableRepo;

  @Autowired
  private ExpenseRepository expenseRepo;

  @Override
  @Transactional(readOnly = true)
  public List<StoryResponse> getAll() {
    List<StoryResponse> storyResponses = new ArrayList<>();
    List<StoryEntity> storyEntities = 
      this.storyRepo.findAll();

    for (StoryEntity storyEntity : storyEntities) {
      StoryResponse storyResponse = 
        buildStoryResponse(storyEntity);

      storyResponses.add(storyResponse);
    }
    
    return storyResponses;
  }

  @Override
  @Transactional(readOnly = true)
  public StoryResponse get(Long id) {
    StoryEntity storyEntity = this.storyRepo
                                  .findById(id)
                                  .orElseThrow(() -> 
                                    new ResourceNotFoundException(
                                      "Story not found with ID: " + id
                                    )
                                  );
    
    return buildStoryResponse(storyEntity);
  }

  @Override
  public StoryResponse create(StoryRequest storyRequest) {
    StoryResponse storyResponse = new StoryResponse();
    StoryEntity storyEntity = new StoryEntity();
    CaseEntity caseEntity = new CaseEntity();

    if (!storyRequest.caseNumber().isBlank()) {
      caseEntity = caseRepo.findByCaseNumber(storyRequest.caseNumber())
                            .orElseThrow(() -> 
                              new ResourceNotFoundException(
                                "Case not found with case number: " + 
                                storyRequest.caseNumber()
                              )
                            );

      storyEntity.setCaseId(caseEntity.getId());
    }

    storyEntity.setTitle(storyRequest.title());
    storyEntity.setParas(storyRequest.paragraphs());
    storyEntity.setCarouselImages(storyRequest.carouselImages());
    storyEntity.setCarousels(storyRequest.carousels());
    storyEntity.setSsFile(
      storyRequest.ssFile().getBytes(StandardCharsets.UTF_8)
    );

    StoryEntity savedStory = this.storyRepo.save(storyEntity);

    storyResponse.setId(savedStory.getId());
    storyResponse.setTitle(savedStory.getTitle());
    storyResponse.setParagraphs(savedStory.getParas());
    storyResponse.setCarouselImages(savedStory.getCarouselImages());
    storyResponse.setCarousels(savedStory.getCarousels());

    FundEntity fundEntity = new FundEntity();
    fundEntity.setAssociatedType(
      storyEntity.getClass().getSimpleName()
    );
    fundEntity.setAssociatedId(savedStory.getId());

    try {
      fundEntity.setGoalAmount(
        Float.parseFloat(storyRequest.fundGoal())
      );
    } catch (NumberFormatException e) {
      log.error("Fund goal value was invalid", e);
    }

    FundEntity savedFund = this.fundRepo.save(fundEntity);

    storyResponse.setFundGoal(Float.toString(savedFund.getGoalAmount()));

    ArrayList<ReceivableEntity> fundsReceived = 
      (ArrayList<ReceivableEntity>) storyRequest.fundsReceived();

    fundsReceived.forEach(fr -> fr.setFundId(savedFund.getId()));

    List<ReceivableEntity> savedreceivableEntities = 
      this.receivableRepo.saveAll(fundsReceived);

    storyResponse.setFundsReceived(
      (ArrayList<ReceivableEntity>) savedreceivableEntities
    );

    ArrayList<ExpenseEntity> fundsUsed = 
      (ArrayList<ExpenseEntity>) storyRequest.fundsUsed();

    fundsUsed.forEach(fu -> fu.setFundId(savedFund.getId()));

    List<ExpenseEntity> savedExpenseEntities = 
      this.expenseRepo.saveAll(fundsUsed);

    storyResponse.setFundsUsed(
      (ArrayList<ExpenseEntity>) savedExpenseEntities
    );

    return storyResponse;
  }
  
  @Override
  public StoryResponse update(Long id, StoryRequest storyRequest) {
    StoryResponse storyResponse = new StoryResponse();
    
    return storyResponse;
  }

  @Override
  public StoryResponse patch(Long id, StoryRequest storyRequest) {
    StoryResponse storyResponse = new StoryResponse();

    StoryEntity storyEntity = this.storyRepo
                                  .findById(id)
                                  .orElseThrow(() -> 
                                    new ResourceNotFoundException(
                                      "Story not found with ID: " + id
                                    )
                                  );
    
    if (!storyRequest.caseNumber().isBlank()) {
      CaseEntity caseEntity = caseRepo.findByCaseNumber(
        storyRequest.caseNumber()
      ).orElseThrow(() -> 
                      new ResourceNotFoundException(
                        "Case not found with case number: " + 
                        storyRequest.caseNumber()
                      )
                    );

      storyEntity.setCaseId(caseEntity.getId());
    }

    if (!storyRequest.title().isBlank()) {
      storyEntity.setTitle(storyRequest.title());
    }

    if (storyRequest.paragraphs().size() > 0) {
      storyEntity.setParas(storyRequest.paragraphs());
    }

    if (storyRequest.carouselImages().size() > 0) {
      storyEntity.setCarouselImages(
        storyRequest.carouselImages()
      );
    }

    if (storyRequest.carousels().size() > 0) {
      storyEntity.setCarousels(storyRequest.carousels());
    }

    if (!storyRequest.ssFile().isBlank()) {
      storyEntity.setSsFile(
        storyRequest.ssFile().getBytes(StandardCharsets.UTF_8)
      );
    }

    storyEntity.setPublish(storyRequest.publish());

    if (!storyRequest.publishDate().isBlank()) {
      try {
        storyEntity.setPublishDate(
          LocalDate.parse(storyRequest.publishDate())
        );
      } catch (DateTimeParseException e) {
        log.error("Error parsing the date string", e);
      }
    }

    StoryEntity savedStory = this.storyRepo.save(storyEntity);

    storyResponse.setId(savedStory.getId());
    storyResponse.setTitle(savedStory.getTitle());
    storyResponse.setParagraphs(savedStory.getParas());
    storyResponse.setCarouselImages(
      savedStory.getCarouselImages()
    );
    storyResponse.setCarousels(savedStory.getCarousels());
    storyResponse.setPublish(storyEntity.getPublish());
    storyResponse.setPublishDate(
      storyEntity.getPublishDate().format(
        DateTimeFormatter.ofPattern("yyyy-MM-dd")
      )
    );

    Optional<FundEntity> fundEntity = 
      this.fundRepo.findbyAssociation(
        storyEntity.getClass().getSimpleName(),
        storyEntity.getId()
      );
    
    if (fundEntity.isPresent()) {
      if (!storyRequest.fundGoal().isBlank()) {
        try {
          fundEntity.get().setGoalAmount(
            Float.parseFloat(storyRequest.fundGoal())
          );
        } catch (NumberFormatException e) {
          log.error("Fund goal value was invalid", e);
        }
      }

      FundEntity savedFund = 
        this.fundRepo.save(fundEntity.get());

      if (storyRequest.fundsReceived().size() > 0) {
        ArrayList<ReceivableEntity> fundsReceived = 
          this.receivableRepo.getByFundId(savedFund.getId());
        
        List<ReceivableEntity> receivableEntities = new ArrayList<>();
        
        for (ReceivableEntity sfr: storyRequest.fundsReceived()) {
          if (sfr.getId() == 0 && 
                !sfr.getReferenceNumber().isBlank()
          ) {
            // new addition
            receivableEntities.add(sfr);
          } else {
            for (ReceivableEntity fr: fundsReceived) {
              if (sfr.getId() == fr.getId()) {
                if (!sfr.getReferenceNumber().isBlank()) {
                  fr.setReferenceNumber(sfr.getReferenceNumber());
                }

                if (sfr.getDate() != null) {
                  fr.setDate(sfr.getDate());
                }

                if (sfr.getAmount() != null) {
                  fr.setAmount(sfr.getAmount());
                }

                if (!sfr.getRemark().isBlank()) {
                  fr.setRemark(sfr.getRemark());
                }

                receivableEntities.add(fr);
              }
            }
          }
        }
        
        List<ReceivableEntity> savedreceivableEntities = 
          this.receivableRepo.saveAll(receivableEntities);

        storyResponse.setFundsReceived(
          (ArrayList<ReceivableEntity>) savedreceivableEntities
        );
      }

      if (storyRequest.fundsUsed().size() > 0) {
        ArrayList<ExpenseEntity> fundsUsed = 
          this.expenseRepo.getByFundId(savedFund.getId());
        
        List<ExpenseEntity> expenseEntities = new ArrayList<>();
        
        for (ExpenseEntity sfr: storyRequest.fundsUsed()) {
          if (sfr.getId() == 0 && !sfr.getName().isBlank()) {
            // new addition
            expenseEntities.add(sfr);
          } else {
            for (ExpenseEntity fr: fundsUsed) {
              if (sfr.getId() == fr.getId()) {
                if (!sfr.getName().isBlank()) {
                  fr.setName(sfr.getName());
                }

                if (sfr.getAmount() != null) {
                  fr.setAmount(sfr.getAmount());
                }

                expenseEntities.add(sfr);
              }
            }
          }
        }

        List<ExpenseEntity> savedExpenseEntities = 
          this.expenseRepo.saveAll(expenseEntities);

        storyResponse.setFundsUsed(
          (ArrayList<ExpenseEntity>) savedExpenseEntities
        );
      }
    }

    return storyResponse;
  }

  @Override
  public void delete(Long id) {
    StoryEntity storyEntity = 
      this.storyRepo.findById(id)
                    .orElseThrow(() -> 
                      new ResourceNotFoundException(
                        "Resource not found with ID: " + id
                      )
                    );
    
    Optional<FundEntity> fundEntity = 
      this.fundRepo.findbyAssociation(
        storyEntity.getClass().getSimpleName(),
        storyEntity.getId()
      );

    if (fundEntity.isPresent()) {
      List<ReceivableEntity> receivableEntities = 
        this.receivableRepo.getByFundId(fundEntity.get().getId());
      List<ExpenseEntity> expenseEntities = 
        this.expenseRepo.getByFundId(fundEntity.get().getId());
      
      this.receivableRepo.deleteAll(receivableEntities);
      this.expenseRepo.deleteAll(expenseEntities);
      this.fundRepo.delete(fundEntity.get());
    }

    List<CommentEntity> commentEntities = 
      this.commentRepo.findbyAssociation(
        storyEntity.getClass().getSimpleName(),
        storyEntity.getId()
      );
    
    this.commentRepo.deleteAll(commentEntities);
    
    this.storyRepo.delete(storyEntity);
  }

  @Override
  public List<CommentEntity> getAllComments(Long storyId) {
    StoryEntity storyEntity = this.storyRepo
                                  .findById(storyId)
                                  .orElseThrow(() -> 
                                    new ResourceNotFoundException(
                                      "Story not found with ID: " + storyId
                                    )
                                  );
    
    List<CommentEntity> commentEntities = 
      this.commentRepo.findbyAssociation(
        storyEntity.getClass().getSimpleName(), storyId
      );
    
    return commentEntities;
  }

  @Override
  public CommentEntity createComment(Long storyId, CommentEntity entity) {
    StoryEntity storyEntity = this.storyRepo
                                  .findById(storyId)
                                  .orElseThrow(() -> 
                                    new ResourceNotFoundException(
                                      "Story not found with ID: " + storyId
                                    )
                                  );
    
    entity.setAssociatedType(storyEntity.getClass().getSimpleName());
    entity.setAssociatedId(storyEntity.getId());
    return this.commentRepo.save(entity);
  }

  @Override
  public CommentEntity patchComment(Long id, CommentEntity entity) {
    CommentEntity commentEntity = this.commentRepo
                                      .findById(id)
                                      .orElseThrow(() -> 
                                        new ResourceNotFoundException(
                                          "Comment not found with ID: " + id
                                        )
                                      );
    
    commentEntity.setApproved(entity.getApproved());
    return this.commentRepo.save(commentEntity);
  }

  private StoryResponse buildStoryResponse(StoryEntity storyEntity) {
    StoryResponse storyResponse = new StoryResponse();

    storyResponse.setId(storyEntity.getId());

    storyResponse.setTitle(storyEntity.getTitle());

    storyResponse.setPublish(storyEntity.getPublish());
    storyResponse.setPublishDate(
      storyEntity.getPublishDate().format(
        DateTimeFormatter.ofPattern("yyyy-MM-dd")
      )
    );

    if (storyEntity.getSsFile() != null) {
      String orgStr = new String(
        storyEntity.getSsFile(),
        StandardCharsets.UTF_8
      );

      storyResponse.setSsFile(orgStr);
    }

    storyResponse.setParagraphs(
      new ArrayList<>(storyEntity.getParas())
    );
    storyResponse.setCarouselImages(
      new ArrayList<>(storyEntity.getCarouselImages())
    );
    storyResponse.setCarousels(
      new ArrayList<>(storyEntity.getCarousels())
    );

    FundEntity fundEntity = 
      fundRepo.findbyAssociation(
        storyEntity.getClass().getSimpleName(),
        storyEntity.getId()
      ).orElseThrow(() -> 
      new ResourceNotFoundException(
        "Fund not found with association type: " + 
        storyEntity.getClass().getSimpleName() +
        " and association ID:" + storyEntity.getId()
      )
    );

    storyResponse.setFundGoal(
      Float.toString(fundEntity.getGoalAmount())
    );

    ArrayList<ReceivableEntity> receivableEntities = 
      this.receivableRepo.getByFundId(fundEntity.getId());
    
    ArrayList<ExpenseEntity> expenseEntities = 
      this.expenseRepo.getByFundId(fundEntity.getId());

    ArrayList<CommentEntity> commentEntities = 
      commentRepo.findbyAssociation(
        storyEntity.getClass().getSimpleName(),
        storyEntity.getId()
      );
      
    storyResponse.setFundsReceived(receivableEntities);
    storyResponse.setFundsUsed(expenseEntities);
    storyResponse.setComments(commentEntities);

    // Optional<FileEntity> fileEntity = this.fileDS.findByAssociation(
    //   storyEntity.getClass().getSimpleName(),
    //   storyEntity.getId()
    // );

    // if (fileEntity.isPresent()) {
    //   storyResponse.setFile(fileEntity.get());
    // }

    return storyResponse;
  }
}
