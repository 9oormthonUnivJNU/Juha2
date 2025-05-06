package com.example.board.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class) // AuditingEntityListener 클래스를 빈팩토리 등록
@Getter
public class BaseEntity {  // 시간 정보를 다루는 클래스 -> BoardEntity가 상속 받음
    @CreationTimestamp
    @Column(updatable = false) // update 시에 관여 안함 설정
    private LocalDateTime createTime;  // 생성 시간

    @UpdateTimestamp
    @Column(insertable = false)  // insert 시에 관여 안함 설정
    private LocalDateTime updateTime;  // 수정 시간
}
// 시간 정보의 결합도를 낮춰 다른 클래스에서도 시간 정보에 접근할 수 있도록 재사용 가능하게 하기위해 상속 관계 설정