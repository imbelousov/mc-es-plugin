package myplugin;

import org.elasticsearch.plugins.Plugin;
import org.elasticsearch.plugins.SearchPlugin;
import org.elasticsearch.search.fetch.FetchSubPhase;

import java.util.Collections;
import java.util.List;

public class MyPlugin extends Plugin implements SearchPlugin {
    @Override
    public List<FetchSubPhase> getFetchSubPhases(FetchPhaseConstructionContext context) {
        return Collections.singletonList(new MyFetchSubPhase());
    }
}
