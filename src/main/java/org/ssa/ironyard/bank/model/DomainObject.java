package org.ssa.ironyard.bank.model;

public interface DomainObject extends Cloneable
{
    Integer getId();
    
    DomainObject clone();

    boolean deeplyEquals(DomainObject obj);
    
    boolean isLoaded();
}
