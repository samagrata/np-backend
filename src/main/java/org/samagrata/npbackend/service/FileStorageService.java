package org.samagrata.npbackend.service;

import java.io.IOException;
import java.util.Base64;
import java.util.Optional;

import org.samagrata.npbackend.entity.BaseEntity;
import org.samagrata.npbackend.entity.FileEntity;
import org.samagrata.npbackend.repository.FileRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileStorageService {
  private final FileRepository fileRepository;

  public FileStorageService(FileRepository fileRepository) {
    this.fileRepository = fileRepository;
  }

  public FileEntity storeFile(
    MultipartFile file,
    BaseEntity associatedEntity
  ) throws IOException {
    String fileName = StringUtils.cleanPath(file.getOriginalFilename());

    try {
      // Check if the file's name contains invalid characters
      if (fileName.contains("..")) {
        throw new IOException(
          "Filename contains invalid path sequence: " + fileName
        );
      }

      FileEntity fileEntity = new FileEntity();
      fileEntity.setName(fileName);
      fileEntity.setType(file.getContentType());
      fileEntity.setFileBlob(file.getBytes());

      fileEntity.setAssociatedType(
        associatedEntity.getClass().getSimpleName()
      );
      fileEntity.setAssociatedId(associatedEntity.getId());
      fileEntity.setB64EncodedFile(
        Base64.getEncoder().encodeToString(
          fileEntity.getFileBlob()
        )
      );

      return fileRepository.save(fileEntity);
    } catch (IOException ex) {
      throw new IOException(
        "Could not store file " + fileName + 
        ". Please try again!", ex
      );
    }
  }

  public FileEntity getFile(Long fileId) {
    return fileRepository
            .findById(fileId)
            .orElseThrow(() -> 
              new RuntimeException(
                "File not found with id " + fileId
              )
            );
  }

  public Optional<FileEntity> findByAssociation(
    String aType, Long aId
  ) {
    return fileRepository.findByAssociation(aType, aId);
  }
}
