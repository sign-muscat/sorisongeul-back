// package com.sorisonsoon.payment.domain.entity;

// import jakarta.persistence.*;
// import lombok.AccessLevel;
// import lombok.NoArgsConstructor;
// import org.springframework.data.annotation.CreatedDate;

// import java.time.LocalDateTime;

// @Entity
// @Table(name = "payment")
// @NoArgsConstructor(access = AccessLevel.PROTECTED)
// public class Payment {

//     @Id
//     @GeneratedValue(strategy = GenerationType.IDENTITY)
//     @Column(name = "payment_id")
//     private Long paymentId;

//     private int payerId;
//     private int amount;

//     @CreatedDate
//     private LocalDateTime payedAt;
// }
package com.sorisonsoon.payment.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "payment")
@Getter
@Setter
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    private Long payerId;
    private int amount;
    private LocalDateTime payedAt;

    // 필요한 경우 추가 필드들...
}