package kr.ac.snu.imlab.ia.hw4;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.eval.RecommenderBuilder;
import org.apache.mahout.cf.taste.eval.RecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.eval.AverageAbsoluteDifferenceRecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.eval.RMSRecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.recommender.svd.Factorizer;
import org.apache.mahout.cf.taste.impl.recommender.svd.SVDPlusPlusFactorizer;
import org.apache.mahout.cf.taste.impl.recommender.svd.SVDRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import java.util.*;
import java.util.concurrent.locks.AbstractQueuedLongSynchronizer;

public class MFRecommender {

    public static void main(String[] args) throws IOException, TasteException {
        Logger.getRootLogger().setLevel(Level.OFF);

        DataModel model = new FileDataModel(new File("data/ua.base"));

        RecommenderBuilder mfRecommenderBuilder = getMFRecommenderBuilder(model); // MF based recommender builder
        RecommenderBuilder ubRecommenderBuilder = getUserBasedRecommenderBuilder(model); // Standard User based recommender builder

        mfBasedRecommend(model, mfRecommenderBuilder);  // TODO: MF based recommendation

        // TODO: 추천 결과 성능 평가
        System.out.println("-- MF based recommender evaluation results --");
        evaluateRecommender(model, mfRecommenderBuilder);
        System.out.println("-- User based recommender evaluation results --");
       // evaluateRecommender(model, ubRecommenderBuilder);
    }

    /**
     * 2-2. SVD++ factorizer의 feature 수와 iteration 수를 변화시키면서
     *      MF based recommender의 추천 결과 성능의 변화 양상 분석하기
     *      : getMFRecommenderBuilder() 메서드 수정
     */
    static RecommenderBuilder getMFRecommenderBuilder(DataModel model) {
        return new RecommenderBuilder() {
            @Override
            public Recommender buildRecommender(DataModel model) throws TasteException {
                // System.out.println("Generating factorizer...");
                Factorizer factorizer = new SVDPlusPlusFactorizer(model, 10, 10);
                    // FIXME: (dataModel, numFeatures, numIterations)
                return new SVDRecommender(model, factorizer);
            }
        };
    }

    static RecommenderBuilder getUserBasedRecommenderBuilder(DataModel model) {
        return new RecommenderBuilder() {
            @Override
            public Recommender buildRecommender(DataModel model) throws TasteException {
                UserSimilarity similarity = new PearsonCorrelationSimilarity(model);
                UserNeighborhood neighborhood =
                    new NearestNUserNeighborhood(100, similarity, model);
                return new GenericUserBasedRecommender(model, neighborhood, similarity);
            }
        };
    }

    /**
     * 2-1. MF based recommender를 완성하고, 1~10번 user에 대한
     *      상위 10개 item의 추천 결과를 txt 파일로 출력하기
     *      : mfBasedRecommend() 메서드 수정
     */
    static void mfBasedRecommend(DataModel model, RecommenderBuilder recommenderBuilder)
            throws IOException, TasteException {
        Recommender recommender = recommenderBuilder.buildRecommender(model);
        int numUsers = model.getNumUsers();
        int numItems = model.getNumItems();
        double[][] score = new double[numUsers][numItems];

        // Calculate user-item score matrix
        for (int u = 0; u < numUsers; u++) {
            for (int i = 0; i < numItems; i++) {
                score[u][i] = recommender.estimatePreference(u+1,i+1);  // e.g. (User 2 - item 3) score = score[1][2]
            }
        }

        // TODO: 적절한 code 입력
        // HINT: Use BufferedWriter instance
        BufferedWriter out = new BufferedWriter(new FileWriter("mf-based-result.txt"));

        for (int u = 0; u < numUsers; u++) {
            double[] userScore = score[u]; // (u+1) 사용자의 전 아이템에 대한 예측평점 배열.  내림차순으로 정렬했을 때 top10뽑기

            // TODO: 적절한 code 입력
            // HINT: Use TreeMap instance to sort elements by key
            TreeMap<Double, Integer> tree = new TreeMap<Double, Integer>(Collections.<Double>reverseOrder());
            for(int i=0;i<numItems;i++){
                tree.put(userScore[i],i);
            }
            System.out.println("numItems is:"+numItems + " numKeys is:" + tree.size());
            System.out.println(tree.keySet());
            for(int i=0; i<10; i++){
                double ls = tree.firstKey();
                int lsv = tree.get(ls);
                //System.out.println("key is " + ls + "\t value(item-id) is "+ lsv);
                String resultRow = (u+1) + ", " + (i+1) + ", " + lsv + ", " + ls;   // "userId, recom_rank, itemId, value"
                //String resultRow = (u+1) + ", " + lsv + ", " + ls;   // "userId, itemId, value"
                System.out.println(resultRow);
                out.write(resultRow);
                out.newLine();
                tree.remove(ls);
            }
            out.flush();
            // TODO: Fix the for loop below
            // for (...) {
            //     String resultRow = (u+1) + ", " + (i+1) + ", " + userScore[u][i];   // "userId, itemId, value"
            //     System.out.println(resultRow);
            // }
            if(u==9)
                break;
        }
        out.close();
    }

    /**
     * 2-2, 2-3. Recommender 평가용 메서드
     */
    static void evaluateRecommender(DataModel model, RecommenderBuilder recommenderBuilder)
            throws TasteException {
        RecommenderEvaluator aadEvaluator = new AverageAbsoluteDifferenceRecommenderEvaluator();
        RecommenderEvaluator rmseEvaluator = new RMSRecommenderEvaluator();

        double aadScore = aadEvaluator.evaluate(recommenderBuilder, null, model, 0.7, 1.0);
        //  (recommenderBuilder, dataModelBuilder, dataModel,trainingPercentage, evaluationPercentage)
        double rmseScore = rmseEvaluator.evaluate(recommenderBuilder, null, model, 0.7, 1.0);

        System.out.println("AAD: " + aadScore);
        System.out.println("RMS: " + rmseScore);
    }
}
