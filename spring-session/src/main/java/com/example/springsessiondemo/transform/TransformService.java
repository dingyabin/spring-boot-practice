package com.example.springsessiondemo.transform;

import com.example.springsessiondemo.entity.SampleData;
import com.example.springsessiondemo.entity.SampleDataCopy;
import com.example.springsessiondemo.entity.TransformTask;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author 丁亚宾
 * Date: 2024/7/3.
 * Time:22:20
 */
@Service
public class TransformService {

    private final long size = 100;

    @Resource
    private SampleDataServiceImpl sampleDataService;

    @Resource
    private SampleDataCopyServiceImpl sampleDataCopyService;

    @Resource
    private TransformTaskServiceImpl transformTaskService;

    private ExecutorService executorService = new ThreadPoolExecutor(10, 50, 1, TimeUnit.MINUTES, new ArrayBlockingQueue<>(500));


    public void doTransform() {
        long startId = 0L;
        while (true) {
            //开始一条任务
            TransformTask transformTask = transformTaskService.newTransformTask(startId, size);
            //查询对应数据
            List<SampleData> simDataByIdOrders = sampleDataService.getSimDataByIdOrders(startId, size);
            //判空
            if (CollectionUtils.isEmpty(simDataByIdOrders)) {
                System.out.println("xxxxxxxxxxxxxxxxxxxxxx");
                break;
            }
            //开启线程执行
            executorService.execute(new TransformJob(simDataByIdOrders.stream().map(SampleDataCopy::bySampleData).collect(Collectors.toList()), transformTask));
            //更新下次开始的id
            startId = simDataByIdOrders.get(simDataByIdOrders.size() - 1).getId();
            //判空
            if (simDataByIdOrders.size() < size) {
                System.out.println("xxxxxxxxxxxxxxxxxxxxxx");
                break;
            }
        }
    }


    public void run(ApplicationArguments args) throws Exception {
        long l = System.currentTimeMillis();
        doTransform();
        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.HOURS);
        System.out.println("vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv" +( System.currentTimeMillis() -l));
    }


    private class TransformJob implements Runnable {
        private final List<SampleDataCopy> sampleDataCopies;

        private final TransformTask transformTask;

        public TransformJob( List<SampleDataCopy> sampleDataCopies, TransformTask transformTask) {
            this.sampleDataCopies = sampleDataCopies;
            this.transformTask = transformTask;
        }

        @Override
        public void run() {
            try {
                sampleDataCopyService.saveBatchByMappers(sampleDataCopies);
                transformTaskService.finish(transformTask.getId());
            } catch (Exception e) {
               e.printStackTrace();
            }
        }
    }

}
