package com.yoma.adminportal.employeemgmt.service.impl;

import com.yoma.adminportal.auditservice.AuditMessage;
import com.yoma.adminportal.auditservice.AuditMessage.EventAction;
import com.yoma.adminportal.auditservice.AuditMessage.EventCategory;
import com.yoma.adminportal.auditservice.AuditMessage.ObjectType;
import com.yoma.auditservice.changeset.AuditChangeSet;
import com.yoma.auditservice.changeset.AuditChangeUtility;
import com.yoma.auditservice.publisher.AuditEventPublisher;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class AbstractAuditableService {

  private final Logger log = LoggerFactory.getLogger(getClass());

  private final AuditEventPublisher auditEventPublisher;

  AbstractAuditableService(
      AuditEventPublisher auditEventPublisher) {
    this.auditEventPublisher = auditEventPublisher;
  }

  void publishAuditEvent(String username, Object oldState, Object newState,
      ObjectType objectType, EventAction eventAction, String description) {
    AuditChangeSet auditChanges = AuditChangeUtility.detectChanges(oldState, newState);
    AuditMessage msg = new AuditMessage()
        .withTimestamp(new Date())
        .withEventCategory(EventCategory.ADMIN_AUDIT)
        .withObjectType(objectType)
        .withEventAction(eventAction)
        .withDescription(description)
        .withUsername(username)
        .withDetails(auditChanges);
    try {
      auditEventPublisher.publish(msg);
    } catch (Throwable t) {
      log.error("Failed to publish audit message", t);
    }
  }

}
