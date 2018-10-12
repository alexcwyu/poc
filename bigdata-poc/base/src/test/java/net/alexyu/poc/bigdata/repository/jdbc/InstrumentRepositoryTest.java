package net.alexyu.poc.bigdata.repository.jdbc;

import com.google.common.collect.Lists;
import net.alexyu.poc.bigdata.config.SpringJdbcConfig;
import net.alexyu.poc.model.proto.Instrument;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static org.junit.Assert.*;

public class InstrumentRepositoryTest {

    private static InstrumentRepository instrumentRepository;

    @BeforeClass
    public static void init() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SpringJdbcConfig.class);
        instrumentRepository = context.getBean(InstrumentRepository.class);
    }

    @Before
    public void setup() {
        instrumentRepository.deleteAll();
    }

    @After
    public void teardown() {
        instrumentRepository.deleteAll();
    }

    @Test
    public void testDefault() {
        assertEquals(0, instrumentRepository.count());
    }


    private static Instrument buildInstrument(String symbol) {
        return Instrument.newBuilder()
                .setInstId(symbol + "@exch")
                .setSymbol(symbol)
                .setExchId("exch")
                .setName("name")
                .setType(Instrument.InstType.STK)
                .setCcyId("usd")
                .setCountryId("us").build();
    }

    @Test
    public void testSaveCountAndFindById() {
        Instrument instrument = buildInstrument("inst1");

        instrumentRepository.save(instrument);

        assertEquals(1, instrumentRepository.count());


        Optional<Instrument> instrument2 = instrumentRepository.findById(instrument.getInstId());

        assertTrue(instrument2.isPresent());

        assertEquals(instrument, instrument2.get());
    }


    @Test
    public void testSaveAllAndFindAll() {
        Instrument inst1 = buildInstrument("inst1");
        Instrument inst2 = buildInstrument("inst2");
        instrumentRepository.saveAll(Lists.newArrayList(inst1, inst2));
        assertEquals(2, instrumentRepository.count());


        List<Instrument> lists = instrumentRepository.findAll();
        assertThat(lists, contains(inst1, inst2));

        List<Instrument> lists2 = instrumentRepository.findAllById(Lists.newArrayList(inst1.getInstId(), inst2.getInstId()));
        assertThat(lists2, contains(inst1, inst2));
    }

    @Test
    public void testInsertBatchAndFindAll() {
        Instrument inst1 = buildInstrument("inst1");
        Instrument inst2 = buildInstrument("inst2");
        Instrument inst3 = buildInstrument("inst3");
        instrumentRepository.insertBatch(Lists.newArrayList(inst1, inst2, inst3));
        assertEquals(3, instrumentRepository.count());


        List<Instrument> lists = instrumentRepository.findAll();
        assertThat(lists, contains(inst1, inst2, inst3));

        List<Instrument> lists2 = instrumentRepository.findAllById(Lists.newArrayList(inst1.getInstId(), inst2.getInstId(), inst3.getInstId()));
        assertThat(lists2, contains(inst1, inst2, inst3));
    }


    @Test
    public void testExistsById() {
        Instrument inst1 = buildInstrument("inst1");

        assertFalse(instrumentRepository.existsById(inst1.getInstId()));

        instrumentRepository.save(inst1);

        assertTrue(instrumentRepository.existsById(inst1.getInstId()));

    }


    @Test
    public void testDeleteById() {
        Instrument inst1 = buildInstrument("inst1");

        instrumentRepository.save(inst1);

        assertTrue(instrumentRepository.existsById(inst1.getInstId()));
        assertEquals(1, instrumentRepository.count());

        instrumentRepository.deleteById(inst1.getInstId());
        assertFalse(instrumentRepository.existsById(inst1.getInstId()));
        assertEquals(0, instrumentRepository.count());
    }

    @Test
    public void testInsertAndUpdate() {
        Instrument inst1 = buildInstrument("inst1");

        instrumentRepository.insert(inst1);
        assertEquals(1, instrumentRepository.count());
        Optional<Instrument> instrumentLoaded = instrumentRepository.findById(inst1.getInstId());
        assertEquals(inst1, instrumentLoaded.get());

        Instrument inst2 = inst1.toBuilder().setGicsIndustry(20).build();
        instrumentRepository.update(inst2);
        assertEquals(1, instrumentRepository.count());
        Optional<Instrument> instrumentLoaded2 = instrumentRepository.findById(inst2.getInstId());
        assertEquals(inst2, instrumentLoaded2.get());
    }

    @Test
    public void testDeleteAll() {

        Instrument inst1 = buildInstrument("inst1");
        Instrument inst2 = buildInstrument("inst2");
        Instrument inst3 = buildInstrument("inst3");

        instrumentRepository.saveAll(Lists.newArrayList(inst1, inst2, inst3));
        assertEquals(3, instrumentRepository.count());
        assertTrue(instrumentRepository.existsById(inst1.getInstId()));
        assertTrue(instrumentRepository.existsById(inst2.getInstId()));
        assertTrue(instrumentRepository.existsById(inst3.getInstId()));

        instrumentRepository.deleteAll();
        assertEquals(0, instrumentRepository.count());

        List<Instrument> list = instrumentRepository.findAll();
        assertTrue(list.size() == 0);
    }

    @Test
    public void testDeleteList() {
        Instrument inst1 = buildInstrument("inst1");
        Instrument inst2 = buildInstrument("inst2");
        Instrument inst3 = buildInstrument("inst3");

        instrumentRepository.saveAll(Lists.newArrayList(inst1, inst2, inst3));

        instrumentRepository.deleteAllById(Lists.newArrayList(inst1.getInstId(), inst2.getInstId()));

        assertEquals(1, instrumentRepository.count());
        assertFalse(instrumentRepository.existsById(inst1.getInstId()));
        assertFalse(instrumentRepository.existsById(inst2.getInstId()));
        assertTrue(instrumentRepository.existsById(inst3.getInstId()));

        List<Instrument> list = instrumentRepository.findAll();
        assertThat(list, contains(inst3));
    }


}
