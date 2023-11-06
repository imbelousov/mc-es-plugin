package myplugin;

import org.apache.lucene.index.LeafReaderContext;
import org.elasticsearch.common.document.DocumentField;
import org.elasticsearch.search.fetch.FetchContext;
import org.elasticsearch.search.fetch.FetchSubPhase;
import org.elasticsearch.search.fetch.FetchSubPhaseProcessor;
import org.elasticsearch.search.fetch.StoredFieldsSpec;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class MyFetchSubPhase implements FetchSubPhase, FetchSubPhaseProcessor {

    public static final String FIELD_LETTER_STATS = "myplugin.letterStats";

    @Override
    public FetchSubPhaseProcessor getProcessor(FetchContext fetchContext) throws IOException {
        return this;
    }

    @Override
    public void setNextReader(LeafReaderContext readerContext) {
    }

    @Override
    public void process(HitContext hitContext) throws IOException {
        var hit = hitContext.hit();
        var source = hitContext.source();
        var stats = new HashMap<Character, Integer>();
        source.source().forEach((name, field) -> analyzeLetters(field, stats));
        var values = stats.entrySet()
                .stream()
                .sorted((o1, o2) -> o2.getValue().compareTo(o1.getValue()))
                .map(i -> i.getKey().toString() + ":" + i.getValue().toString())
                .map(i -> (Object) i)
                .collect(Collectors.toList());
        var field = new DocumentField(FIELD_LETTER_STATS, values);
        hit.setDocumentField(FIELD_LETTER_STATS, field);
    }

    @Override
    public StoredFieldsSpec storedFieldsSpec() {
        return new StoredFieldsSpec(true, false, new HashSet<>());
    }

    private void analyzeLetters(Object value, Map<Character, Integer> stats) {
        if (value instanceof String s)
            analyzeLetters(s, stats);
        if(value instanceof List<?> l && !l.isEmpty() && l.get(0) instanceof String)
            l.forEach(i -> analyzeLetters((String) i, stats));
    }

    private void analyzeLetters(String value, Map<Character, Integer> stats) {
        value
                .toLowerCase(Locale.ROOT)
                .chars()
                .filter(Character::isLetter)
                .forEach(c -> stats.put((char) c, stats.getOrDefault((char) c, 0) + 1));
    }
}
