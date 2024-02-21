package com.garakaniirhf.spring.data.jpa.pagingsorting.model;


import jakarta.persistence.*;

@Entity
@Table(name = "tutorials")
public class Book {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column(name = "caption")
	private String caption;

	@Column(name = "comment")
	private String comment;

	@Column(name = "rated")
	private boolean rated;

	public Book() {

	}

	public Book(String caption, String comment, boolean rated) {
		this.caption = caption;
		this.comment = comment;
		this.rated = rated;
	}

	

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public boolean isRated() {
		return rated;
	}

	public void setRated(boolean rated) {
		this.rated = rated;
	}

	@Override
	public String toString() {
		return "Book [id=" + id + ", caption=" + caption + ", desc=" + comment + ", rated=" + rated + "]";
	}

}
