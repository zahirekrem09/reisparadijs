package com.reisparadijs.reisparadijs.business.domain;

/**
 * @author Zahir Ekrem SARITEKE
 * @project reisparadijs
 * @created 09 August Friday 2024 - 10:48
 */
public class MessageSubject {

    private Integer id;
    private String title;

    public MessageSubject() {
    }

    public MessageSubject(Integer id, String title) {
        this.id = id;
        this.title = title;
    }

    public MessageSubject( int id) {
      this(id, null);
    }

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setId(Integer id) {
        this.id = id;
    }

}
