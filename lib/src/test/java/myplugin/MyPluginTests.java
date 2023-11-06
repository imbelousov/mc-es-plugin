package myplugin;

import org.elasticsearch.cluster.metadata.IndexMetadata;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.plugins.Plugin;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.test.ESIntegTestCase;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Collection;
import java.util.Collections;

@RunWith(com.carrotsearch.randomizedtesting.RandomizedRunner.class)
public class MyPluginTests extends ESIntegTestCase {
    @Test
    public void letter_stats_test() throws Exception {
        client()
                .prepareIndex("my_index")
                .setSource(
                        "first_field", "Ave Samael, Princeps Tenebrarum",
                        "second_field", "Es Diabolus Magnus senior dei lucis",
                        "my_number", 100,
                        "my_string_array", new String[]{"Ipsus", "Deus", "Chaosis", "Pater", "Ater"},
                        "my_number_array", new Integer[]{1, 2, 4}
                )
                .get();

        refresh();

        var response = client()
                .prepareSearch("my_index")
                .setQuery(QueryBuilders.matchAllQuery())
                .setSource(new SearchSourceBuilder().fetchSource(new String[]{"first_field", "my_number"}, null))
                .execute()
                .actionGet();

        var stats = response
                .getHits()
                .getHits()[0]
                .getFields()
                .get(MyFetchSubPhase.FIELD_LETTER_STATS)
                .getValues();

        assertEquals(stats.size(), 18);
    }

    @Override
    protected Settings nodeSettings(int nodeOrdinal, Settings otherSettings) {
        return Settings.builder().put(super.nodeSettings(nodeOrdinal, otherSettings))
                .put("network.host", "localhost")
                .put("http.port", "9200")
                .build();
    }

    @Override
    public Settings indexSettings() {
        return Settings.builder()
                .put(IndexMetadata.SETTING_NUMBER_OF_SHARDS, 1)
                .put(IndexMetadata.SETTING_NUMBER_OF_REPLICAS, 0)
                .build();
    }

    @Override
    protected Collection<Class<? extends Plugin>> nodePlugins() {
        return Collections.singletonList(MyPlugin.class);
    }
}
