package org.samagrata.npbackend.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.samagrata.npbackend.entity.CommentEntity;
import org.samagrata.npbackend.entity.ExpenseEntity;
import org.samagrata.npbackend.entity.ReceivableEntity;
import lombok.Data;

@Data
public class StoryResponse {
  Long id;
  String title = "";
	List<String> paragraphs = new ArrayList<>();
	List<String> carouselImages = new ArrayList<>();
	List<Map<String, String>> carousels = new ArrayList<>();
	String fundGoal = "";
	List<ReceivableEntity> fundsReceived = new ArrayList<>();
	List<ExpenseEntity> fundsUsed = new ArrayList<>();
	String ssFile = null;
  Boolean publish = false;
	String publishDate = "";
  List<CommentEntity> comments = new ArrayList<>();
}
