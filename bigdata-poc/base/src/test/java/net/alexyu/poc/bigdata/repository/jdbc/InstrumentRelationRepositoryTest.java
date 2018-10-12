package net.alexyu.poc.bigdata.repository.jdbc;

import com.google.common.collect.Lists;
import net.alexyu.poc.bigdata.config.SpringJdbcConfig;
import net.alexyu.poc.model.proto.InstrumentRelation;
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

public class InstrumentRelationRepositoryTest {

    private static InstrumentRelationRepository instrumentRelationRepository;

    @BeforeClass
    public static void init() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SpringJdbcConfig.class);
        instrumentRelationRepository = context.getBean(InstrumentRelationRepository.class);
    }

    @Before
    public void setup() {
        instrumentRelationRepository.deleteAll();
    }

    @After
    public void teardown() {
        instrumentRelationRepository.deleteAll();
    }

    @Test
    public void testDefault() {
        assertEquals(0, instrumentRelationRepository.count());
    }

    private static InstrumentRelation buildInstrumentRelation(String instId, String constInstId) {
        return InstrumentRelation.newBuilder()
                .setInstId(instId)
                .setConstInstId(constInstId)
                .setWeight(0.5)
                .build();
    }


    @Test
    public void testSaveCountAndFindById() {
        InstrumentRelation ir1 = buildInstrumentRelation("instId1", "constInstId");

        instrumentRelationRepository.save(ir1);

        assertEquals(1, instrumentRelationRepository.count());


        Optional<InstrumentRelation> ir2 = instrumentRelationRepository.findById(Pair.of(ir1.getInstId(), ir1.getConstInstId()));

        assertTrue(ir2.isPresent());

        assertEquals(ir1, ir2.get());
    }


    @Test
    public void testSaveAllAndFindAll() {
        InstrumentRelation ir1 = buildInstrumentRelation("instId1", "constInstId1");
        InstrumentRelation ir2 = buildInstrumentRelation("instId1", "constInstId2");
        instrumentRelationRepository.saveAll(Lists.newArrayList(ir1, ir2));
        assertEquals(2, instrumentRelationRepository.count());


        List<InstrumentRelation> lists = instrumentRelationRepository.findAll();
        assertThat(lists, contains(ir1, ir2));

        List<InstrumentRelation> lists2 = instrumentRelationRepository.findAllById(Lists.newArrayList(Pair.of(ir1.getInstId(), ir1.getConstInstId()), Pair.of(ir2.getInstId(), ir2.getConstInstId())));
        assertThat(lists2, contains(ir1, ir2));
    }


    @Test
    public void testExistsById() {
        InstrumentRelation ir1 = buildInstrumentRelation("instId1", "constInstId1");
        Pair<String, String> id = Pair.of(ir1.getInstId(), ir1.getConstInstId());

        assertFalse(instrumentRelationRepository.existsById(id));

        instrumentRelationRepository.save(ir1);

        assertTrue(instrumentRelationRepository.existsById(id));

    }


    @Test
    public void testDeleteById() {
        InstrumentRelation ir1 = buildInstrumentRelation("instId1", "constInstId1");
        Pair<String, String> id = Pair.of(ir1.getInstId(), ir1.getConstInstId());

        instrumentRelationRepository.save(ir1);


        assertTrue(instrumentRelationRepository.existsById(id));
        assertEquals(1, instrumentRelationRepository.count());

        instrumentRelationRepository.deleteById(id);
        assertFalse(instrumentRelationRepository.existsById(id));
        assertEquals(0, instrumentRelationRepository.count());
    }

    @Test
    public void testInsertAndUpdate() {
        InstrumentRelation ir1 = buildInstrumentRelation("instId1", "constInstId1");
        Pair<String, String> id = Pair.of(ir1.getInstId(), ir1.getConstInstId());

        instrumentRelationRepository.insert(ir1);
        assertEquals(1, instrumentRelationRepository.count());
        Optional<InstrumentRelation> irLoaded = instrumentRelationRepository.findById(id);
        assertEquals(ir1, irLoaded.get());

        InstrumentRelation ir2 = ir1.toBuilder().setWeight(1.0).build();
        instrumentRelationRepository.update(ir2);
        assertEquals(1, instrumentRelationRepository.count());
        Optional<InstrumentRelation> irLoaded2 = instrumentRelationRepository.findById(id);
        assertEquals(ir2, irLoaded2.get());
    }

    @Test
    public void testDeleteAll() {

        InstrumentRelation ir1 = buildInstrumentRelation("instId1", "constInstId1");
        InstrumentRelation ir2 = buildInstrumentRelation("instId1", "constInstId2");
        InstrumentRelation ir3 = buildInstrumentRelation("instId1", "constInstId3");
        Pair<String, String> id1 = Pair.of(ir1.getInstId(), ir1.getConstInstId());
        Pair<String, String> id2 = Pair.of(ir2.getInstId(), ir2.getConstInstId());
        Pair<String, String> id3 = Pair.of(ir3.getInstId(), ir3.getConstInstId());

        instrumentRelationRepository.saveAll(Lists.newArrayList(ir1, ir2, ir3));
        assertEquals(3, instrumentRelationRepository.count());
        assertTrue(instrumentRelationRepository.existsById(id1));
        assertTrue(instrumentRelationRepository.existsById(id2));
        assertTrue(instrumentRelationRepository.existsById(id3));

        instrumentRelationRepository.deleteAll();
        assertEquals(0, instrumentRelationRepository.count());

        List<InstrumentRelation> list = instrumentRelationRepository.findAll();
        assertTrue(list.size() == 0);
    }

    @Test
    public void testDeleteList() {
        InstrumentRelation ir1 = buildInstrumentRelation("instId1", "constInstId1");
        InstrumentRelation ir2 = buildInstrumentRelation("instId1", "constInstId2");
        InstrumentRelation ir3 = buildInstrumentRelation("instId1", "constInstId3");
        Pair<String, String> id1 = Pair.of(ir1.getInstId(), ir1.getConstInstId());
        Pair<String, String> id2 = Pair.of(ir2.getInstId(), ir2.getConstInstId());
        Pair<String, String> id3 = Pair.of(ir3.getInstId(), ir3.getConstInstId());

        instrumentRelationRepository.saveAll(Lists.newArrayList(ir1, ir2, ir3));

        instrumentRelationRepository.deleteAllById(Lists.newArrayList(id1, id2));

        assertEquals(1, instrumentRelationRepository.count());
        assertFalse(instrumentRelationRepository.existsById(id1));
        assertFalse(instrumentRelationRepository.existsById(id2));
        assertTrue(instrumentRelationRepository.existsById(id3));

        List<InstrumentRelation> list = instrumentRelationRepository.findAll();
        assertThat(list, contains(ir3));
    }


}
