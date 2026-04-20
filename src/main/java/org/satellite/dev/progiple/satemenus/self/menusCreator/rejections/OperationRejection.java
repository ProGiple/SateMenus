package org.satellite.dev.progiple.satemenus.self.menusCreator.rejections;

import lombok.Getter;
import lombok.Setter;
import org.novasparkle.lunaspring.API.conditions.Conditions;

@Getter @Setter
public class OperationRejection implements Rejection<Conditions.Operation> {
    public Conditions.Operation value;
    public OperationRejection(Conditions.Operation operation) {
        this.value = operation;
    }
}
