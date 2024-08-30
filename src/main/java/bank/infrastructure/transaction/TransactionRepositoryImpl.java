package bank.infrastructure.transaction;

import bank.domain.transaction.Transaction;
import bank.domain.transaction.TransactionRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class TransactionRepositoryImpl implements TransactionRepository {

    private final TransactionJpaRepository transactionJpaRepository;
    private final EntityManager em;

    @Override
    public Transaction save(Transaction transaction) {
        return transactionJpaRepository.save(transaction);
    }


    @Override
    public List<Transaction> findTransactionList(Long accountId, String gubun, Integer page) {
        // 동적 쿼리(gubun값을 가지고 동적 쿼리 = DEPOSIT(입금),WITHDRAW(출금),ALL(입출금내역))
        String sql ="";
        sql+="select t from Transaction t ";

        if (gubun.equals("WITHDRAW")){
            sql += "join fetch t.withdrawAccount wa ";
            sql += "where t.withdrawAccount.id = :withdrawAccountId";
        }else if (gubun.equals("DEPOSIT")){
            sql += "join fetch t.depositAccount da ";
            sql += "where t.depositAccount.id = :depositAccountId";
        }else {
            sql += "left join fetch t.withdrawAccount wa ";
            sql += "left join fetch t.depositAccount da ";
            sql += "where t.withdrawAccount.id = :withdrawAccountId ";
            sql += "or ";
            sql += "t.depositAccount.id = :depositAccountId";
        }

        TypedQuery<Transaction> query = em.createQuery(sql, Transaction.class);

        if (gubun.equals("WITHDRAW")){
            query = query.setParameter("withdrawAccountId",accountId);
        }else if (gubun.equals("DEPOSIT")){
            query = query.setParameter("depositAccountId",accountId);
        }else {
            query = query.setParameter("withdrawAccountId",accountId);
            query = query.setParameter("depositAccountId",accountId);
        }

        query.setFirstResult(page * 5); // 5,10,15
        query.setMaxResults(5);
        return query.getResultList();
    }
}
