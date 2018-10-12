package net.alexyu.poc.bigdata.repository.springdata;

import net.alexyu.poc.entity.Account;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends CrudRepository<Account, String> {

}
