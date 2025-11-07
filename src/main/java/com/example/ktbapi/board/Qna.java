package com.example.ktbapi.board;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@DiscriminatorValue("QNA")
@Getter @Setter
public class Qna extends Board {

    @Column(nullable = false)
    private boolean solved;
}
