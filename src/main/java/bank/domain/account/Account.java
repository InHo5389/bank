package bank.domain.account;

import bank.domain.common.exception.CustomGlobalException;
import bank.domain.common.exception.ErrorType;
import bank.domain.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class)
@Table(name = "account_tb")
@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql="UPDATE account_tb Set deleted = true where id = ?")
@SQLRestriction("deleted is false")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Column(unique = true,nullable = false,length = 4)
    private Long number;
    @Column(nullable = false,length = 4)
    private Long password;
    @Column(length = 4)
    private Long balance; // 잔액(기본값 1000원)

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;
    private boolean deleted;

    public void checkOwner(Long userId){
        if (user.getId() != userId){
            throw new CustomGlobalException(ErrorType.INVALID_ACCOUNT_OWNER);
        }
    }
}
