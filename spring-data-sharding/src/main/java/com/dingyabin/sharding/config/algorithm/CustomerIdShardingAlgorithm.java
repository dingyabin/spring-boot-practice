package com.dingyabin.sharding.config.algorithm;

import com.google.common.collect.Range;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;
import org.apache.shardingsphere.api.sharding.standard.RangeShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.RangeShardingValue;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @author 丁亚宾
 * Date: 2024/10/29.
 * Time:13:16
 */
public class CustomerIdShardingAlgorithm implements PreciseShardingAlgorithm<Integer>, RangeShardingAlgorithm<Integer> {


    @Override
    public String doSharding(Collection<String> targets, PreciseShardingValue<Integer> shardingValue) {
        Integer number = shardingValue.getValue();
        String logicTableName = shardingValue.getLogicTableName();
        String table = logicTableName + "_" + number % targets.size();
        if (targets.contains(table)) {
            return table;
        }
        throw new UnsupportedOperationException();
    }


    @Override
    public Collection<String> doSharding(Collection<String> targets, RangeShardingValue<Integer> shardingValue) {
        String logicTableName = shardingValue.getLogicTableName();
        Range<Integer> valueRange = shardingValue.getValueRange();
        Integer lowerEndpoint = valueRange.lowerEndpoint();
        Integer upperEndpoint = valueRange.upperEndpoint();
        if (lowerEndpoint > upperEndpoint) {
            throw new UnsupportedOperationException();
        }
        Set<String> tables = new HashSet<>();
        for (long i = lowerEndpoint; i <= upperEndpoint; i++) {
            tables.add(logicTableName + "_" + i % targets.size());
            //已经需要全表扫描了，就不要再往后走了。。。。
            if (targets.size() == tables.size()) {
                break;
            }
        }
        return tables;
    }
}
