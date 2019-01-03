package com.yoma.adminportal.employeemgmt.service.impl.operation;

/**
 * Class to hold the Role to Entity changes
 *
 * @author TSNR
 */
public class AbstractEntityOperation<T> {

  private final EntityOperation operation;
  private final T entity;

  AbstractEntityOperation(EntityOperation operation, T entity) {
    super();
    this.operation = operation;
    this.entity = entity;
  }

  public EntityOperation getOperation() {
    return operation;
  }

  public T getEntity() {
    return entity;
  }

}
