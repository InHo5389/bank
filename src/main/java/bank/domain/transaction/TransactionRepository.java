package bank.domain.transaction;

import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TransactionRepository {

    Transaction save(Transaction transaction);
    List<Transaction> findTransactionList(@Param("accountId")Long accountId,
                                          @Param("gubun") String gubun,
                                          @Param("page")Integer page);
}
