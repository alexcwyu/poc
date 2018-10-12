package net.alexyu.poc.bigdata.repository.jdbc;

import com.google.common.collect.Lists;
import net.alexyu.poc.bigdata.config.SpringJdbcConfig;
import net.alexyu.poc.model.proto.Position;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static org.junit.Assert.*;

public class PositionRepositoryTest {

    private static PositionRepository positionRepository;

    @BeforeClass
    public static void init() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SpringJdbcConfig.class);
        positionRepository = context.getBean(PositionRepository.class);
    }

    @Before
    public void setup() {
        positionRepository.deleteAll();
    }

    @After
    public void teardown() {
        positionRepository.deleteAll();
    }

    @Test
    public void testDefault() {
        assertEquals(0, positionRepository.count());
    }

    private static Position buildPosition(String acctId, String instId) {
        return Position.newBuilder()
                .setAcctId(acctId)
                .setInstId(instId)
                .setTotalQty(1.0)
                .setAvgPrice(1.0)
                .build();
    }


    @Test
    public void testSaveCountAndFindById() {
        Position position1 = buildPosition("acctId1", "instId1");

        positionRepository.save(position1);

        assertEquals(1, positionRepository.count());


        Optional<Position> position2 = positionRepository.findById(Pair.of(position1.getAcctId(), position1.getInstId()));

        assertTrue(position2.isPresent());

        assertEquals(position1, position2.get());
    }


    @Test
    public void testSaveAllAndFindAll() {
        Position position1 = buildPosition("acctId1", "instId1");
        Position position2 = buildPosition("acctId1", "instId2");
        positionRepository.saveAll(Lists.newArrayList(position1, position2));
        assertEquals(2, positionRepository.count());


        List<Position> lists = positionRepository.findAll();
        assertThat(lists, contains(position1, position2));

        List<Position> lists2 = positionRepository.findAllById(Lists.newArrayList(Pair.of(position1.getAcctId(), position1.getInstId()), Pair.of(position2.getAcctId(), position2.getInstId())));
        assertThat(lists2, contains(position1, position2));
    }


    @Test
    public void testExistsById() {
        Position position1 = buildPosition("acctId1", "instId1");
        Pair<String, String> id = Pair.of(position1.getAcctId(), position1.getInstId());

        assertFalse(positionRepository.existsById(id));

        positionRepository.save(position1);

        assertTrue(positionRepository.existsById(id));

    }


    @Test
    public void testDeleteById() {
        Position position1 = buildPosition("acctId1", "instId1");
        Pair<String, String> id = Pair.of(position1.getAcctId(), position1.getInstId());

        positionRepository.save(position1);


        assertTrue(positionRepository.existsById(id));
        assertEquals(1, positionRepository.count());

        positionRepository.deleteById(id);
        assertFalse(positionRepository.existsById(id));
        assertEquals(0, positionRepository.count());
    }

    @Test
    public void testInsertAndUpdate() {
        Position position1 = buildPosition("acctId1", "instId1");
        Pair<String, String> id = Pair.of(position1.getAcctId(), position1.getInstId());

        positionRepository.insert(position1);
        assertEquals(1, positionRepository.count());
        Optional<Position> posLoaded = positionRepository.findById(id);
        assertEquals(position1, posLoaded.get());

        Position position2 = position1.toBuilder().setTotalQty(2.0).setAvgPrice(2.0).build();
        positionRepository.update(position2);
        assertEquals(1, positionRepository.count());
        Optional<Position> posLoaded2 = positionRepository.findById(id);
        assertEquals(position2, posLoaded2.get());
    }

    @Test
    public void testDeleteAll() {

        Position position1 = buildPosition("acctId1", "instId1");
        Position position2 = buildPosition("acctId1", "instId2");
        Position position3 = buildPosition("acctId1", "instId3");
        Pair<String, String> id1 = Pair.of(position1.getAcctId(), position1.getInstId());
        Pair<String, String> id2 = Pair.of(position2.getAcctId(), position2.getInstId());
        Pair<String, String> id3 = Pair.of(position3.getAcctId(), position3.getInstId());

        positionRepository.saveAll(Lists.newArrayList(position1, position2, position3));
        assertEquals(3, positionRepository.count());
        assertTrue(positionRepository.existsById(id1));
        assertTrue(positionRepository.existsById(id2));
        assertTrue(positionRepository.existsById(id3));

        positionRepository.deleteAll();
        assertEquals(0, positionRepository.count());

        List<Position> list = positionRepository.findAll();
        assertTrue(list.size() == 0);
    }

    @Test
    public void testDeleteList() {
        Position position1 = buildPosition("acctId1", "instId1");
        Position position2 = buildPosition("acctId1", "instId2");
        Position position3 = buildPosition("acctId1", "instId3");
        Pair<String, String> id1 = Pair.of(position1.getAcctId(), position1.getInstId());
        Pair<String, String> id2 = Pair.of(position2.getAcctId(), position2.getInstId());
        Pair<String, String> id3 = Pair.of(position3.getAcctId(), position3.getInstId());

        positionRepository.saveAll(Lists.newArrayList(position1, position2, position3));

        positionRepository.deleteAllById(Lists.newArrayList(id1, id2));

        assertEquals(1, positionRepository.count());
        assertFalse(positionRepository.existsById(id1));
        assertFalse(positionRepository.existsById(id2));
        assertTrue(positionRepository.existsById(id3));

        List<Position> list = positionRepository.findAll();
        assertThat(list, contains(position3));
    }


}
