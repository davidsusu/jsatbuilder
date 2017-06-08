package hu.webarticum.jsatbuilder.builder.core;


public abstract class AbstractConstraint implements Constraint {
    
    private boolean removed;
    
    private boolean required;

    private final DependencyManager dependencyManager;
    
    public AbstractConstraint(boolean required) {
        this.required = required;
        dependencyManager = new DependencyManager(this);
    }
    
    @Override
    public boolean isRequired() {
        return required;
    }

    @Override
    public void remove() throws CollapseException {
        unset();
        if (required) {
            throw new CollapseException(this);
        }
    }

    @Override
    public void unset() {
        removed = true;
        dependencyManager.unlinkDependencies();
    }

    @Override
    public boolean isRemoved() {
        return removed;
    }

    @Override
    public DependencyManager getDependencyManager() {
        return dependencyManager;
    }
    
}