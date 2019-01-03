package com.yoma.adminportal.employeemgmt.service.impl.exception;

public class EntityNotFoundException extends Exception {

  private static final long serialVersionUID = 1L;

  private final Class<?> entityClass;

  private final Object searchKey;

  public EntityNotFoundException(Class<?> entityClass, Object searchKey) {
    super();
    this.entityClass = entityClass;
    this.searchKey = searchKey;
  }

  public Class<?> getEntityClass() {
    return entityClass;
  }

  public Object getSearchKey() {
    return searchKey;
  }

  @Override
  public String toString() {
    return "EntityNotFoundException [entityClass=" + entityClass + ", searchKey=" + searchKey + "]";
  }

}
