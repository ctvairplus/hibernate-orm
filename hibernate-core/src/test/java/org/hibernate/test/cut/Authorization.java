package org.hibernate.test.cut;

public class Authorization {

  private Long id;
  private MonetoryAmount value;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public MonetoryAmount getValue() {
    return value;
  }

  public void setValue(MonetoryAmount value) {
    this.value = value;
  }
}
