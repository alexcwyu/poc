package net.alexyu.poc.bigdata.repository.jdbc;

import com.google.common.collect.Lists;
import net.alexyu.poc.bigdata.config.SpringJdbcConfig;
import net.alexyu.poc.model.proto.Account;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static org.junit.Assert.*;

public class AccountRepositoryTest {

    private static AccountRepository accountRepository;

    @BeforeClass
    public static void init() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SpringJdbcConfig.class);
        accountRepository = context.getBean(AccountRepository.class);
    }

    @Before
    public void setup() {
        accountRepository.deleteAll();
    }

    @After
    public void teardown() {
        accountRepository.deleteAll();
    }

    @Test
    public void testDefault() {
        assertEquals(0, accountRepository.count());
    }


    private static Account buildAccount(String accountId) {
        return Account.newBuilder()
                .setAcctId(accountId)
                .setBusGroup(Account.BusGroup.FID)
                .setRegion(Account.Region.NY)
                .setFirmName("parent").build();
    }

    @Test
    public void testSaveCountAndFindById() {
        Account account = buildAccount("acct1");

        accountRepository.save(account);

        assertEquals(1, accountRepository.count());


        Optional<Account> account2 = accountRepository.findById(account.getAcctId());

        assertTrue(account2.isPresent());

        assertEquals(account, account2.get());
    }


    @Test
    public void testSaveAllAndFindAll() {

        Account account1 = buildAccount("acct1");

        Account account2 = buildAccount("acct2");
        accountRepository.saveAll(Lists.newArrayList(account1, account2));
        assertEquals(2, accountRepository.count());


        List<Account> lists = accountRepository.findAll();
        assertThat(lists, contains(account1, account2));

        List<Account> lists2 = accountRepository.findAllById(Lists.newArrayList(account1.getAcctId(), account2.getAcctId()));
        assertThat(lists2, contains(account1, account2));
    }


    @Test
    public void testExistsById() {
        Account account1 = buildAccount("acct1");

        assertFalse(accountRepository.existsById(account1.getAcctId()));

        accountRepository.save(account1);

        assertTrue(accountRepository.existsById(account1.getAcctId()));

    }


    @Test
    public void testDeleteById() {
        Account account1 = buildAccount("acct1");

        accountRepository.save(account1);

        assertTrue(accountRepository.existsById(account1.getAcctId()));
        assertEquals(1, accountRepository.count());

        accountRepository.deleteById(account1.getAcctId());
        assertFalse(accountRepository.existsById(account1.getAcctId()));
        assertEquals(0, accountRepository.count());
    }

    @Test
    public void testInsertAndUpdate() {
        Account account1 = buildAccount("acct1");

        accountRepository.insert(account1);
        assertEquals(1, accountRepository.count());
        Optional<Account> accountLoaded = accountRepository.findById(account1.getAcctId());
        assertEquals(account1, accountLoaded.get());

        Account account2 = account1.toBuilder().setFirmName("parent2").build();
        accountRepository.update(account2);
        assertEquals(1, accountRepository.count());
        Optional<Account> accountLoaded2 = accountRepository.findById(account2.getAcctId());
        assertEquals(account2, accountLoaded2.get());
    }

    @Test
    public void testDeleteAll() {

        Account account1 = buildAccount("acct1");
        Account account2 = buildAccount("acct2");
        Account account3 = buildAccount("acct3");

        accountRepository.saveAll(Lists.newArrayList(account1, account2, account3));
        assertEquals(3, accountRepository.count());
        assertTrue(accountRepository.existsById(account1.getAcctId()));
        assertTrue(accountRepository.existsById(account2.getAcctId()));
        assertTrue(accountRepository.existsById(account3.getAcctId()));

        accountRepository.deleteAll();
        assertEquals(0, accountRepository.count());

        List<Account> list = accountRepository.findAll();
        assertTrue(list.size() == 0);
    }

    @Test
    public void testDeleteList() {
        Account account1 = buildAccount("acct1");
        Account account2 = buildAccount("acct2");
        Account account3 = buildAccount("acct3");

        accountRepository.saveAll(Lists.newArrayList(account1, account2, account3));

        accountRepository.deleteAllById(Lists.newArrayList(account1.getAcctId(), account2.getAcctId()));

        assertEquals(1, accountRepository.count());
        assertFalse(accountRepository.existsById(account1.getAcctId()));
        assertFalse(accountRepository.existsById(account2.getAcctId()));
        assertTrue(accountRepository.existsById(account3.getAcctId()));

        List<Account> list = accountRepository.findAll();
        assertThat(list, contains(account3));
    }


}
