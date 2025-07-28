package org.samagrata.npbackend.dto;

import java.util.List;
import java.util.Map;

import org.samagrata.npbackend.entity.ExpenseEntity;
import org.samagrata.npbackend.entity.ReceivableEntity;

public record StoryRequest(
  String caseNumber,
  String title,
	List<String> paragraphs,
	List<String> carouselImages,
	List<Map<String, String>> carousels,
	String fundGoal,
	List<ReceivableEntity> fundsReceived,
	List<ExpenseEntity> fundsUsed,
	String ssFile,
  Boolean publish,
	String publishDate
) {}
