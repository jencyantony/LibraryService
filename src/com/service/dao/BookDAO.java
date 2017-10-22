package com.service.dao;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name="book_details")
@Table(name="book_details", schema="hr")
public class BookDAO {

	@Id
	private int id;
	private String title;
	private String author;
	private int borrowedBy;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public int getBorrowedBy() {
		return borrowedBy;
	}
	public void setBorrowedBy(int borrowedBy) {
		this.borrowedBy = borrowedBy;
	}
}
