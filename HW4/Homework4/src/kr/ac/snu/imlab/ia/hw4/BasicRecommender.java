package kr.ac.snu.imlab.ia.hw4;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.common.LongPrimitiveIterator;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.ThresholdUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.UncenteredCosineSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.LogLikelihoodSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.UserBasedRecommender;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import java.util.List;

public class BasicRecommender {

    public static void main(String[] args) throws IOException, TasteException {
        Logger.getRootLogger().setLevel(Level.OFF);

        DataModel model = new FileDataModel(new File("data/ua.base")); //MovieLens 데이터를 프로그램 상에 로
        userBasedRecommend(model);  // TODO: User based recommendation
        itemBasedRecommend(model);  // TODO: Item based recommendation
    }

    /**
     * 1-1. User based recommender를 완성하고, 1~10번 user에 대한
     *      상위 10개 item의 추천 결과를 txt 파일로 출력하기
     * 1-3. 유사도 함수를 변경해 가면서 1-1 과정을 반복하기
     *      : userBasedRecommend() 메서드 수정
     */
    static void userBasedRecommend(DataModel model) throws IOException, TasteException {
        // FIXME: Choose one of similarity functions below:
        UserSimilarity similarity = new PearsonCorrelationSimilarity(model);
        // UserSimilarity similarity = new UncenteredCosineSimilarity(model);
        //UserSimilarity similarity = new LogLikelihoodSimilarity(model);

        UserNeighborhood neighborhood = new ThresholdUserNeighborhood(0.1, similarity, model);
            // (simThreshold, userSimilarity, dataModel)

        UserBasedRecommender recommender =
                new GenericUserBasedRecommender(model, neighborhood, similarity); // 지정한 유사도 함수와 네이버후드 선정 규칙을 적용한 User-based recommender 적용
        // TODO: 적절한 code 입력
        // HINT: Use BufferedWriter instance
        BufferedWriter out = new BufferedWriter(new FileWriter("user-based-result.txt"));
        LongPrimitiveIterator users = model.getUserIDs();
        while (users.hasNext()) {
            long userId = users.nextLong();
            // TODO: 적절한 code 입력
            // HINT: Use if statement + break
            if(userId<11) {
                System.out.println("\nuserId is : " + userId);
                List<RecommendedItem> recommendations = recommender.recommend(userId, 10);  // FIXME: (userId, howMany) (top howMany) items

                for (RecommendedItem recommendation : recommendations) {
                    String resultRow = userId + ", " + recommendation.getItemID() +
                            ", " + recommendation.getValue();
                    // "userId, itemId, value"
                    System.out.println(resultRow);
                    out.write(resultRow);
                    out.newLine();
                }
            }else{
                 break;
            }
        }
        out.flush();
        out.close();
    }

    /**
     * 1-2. Item based recommender를 완성하고, 1~10번 item에 대한
     *      상위 10개 item의 추천 결과를 txt 파일로 출력하기
     * 1-3. 유사도 함수를 변경해 가면서 1-2 과정을 반복하기
     *      : itemBasedRecommend() 메서드 수정
     */
    static void itemBasedRecommend(DataModel model) throws IOException, TasteException {
        // FIXME: Choose one of similarity functions below:
        ItemSimilarity similarity = new PearsonCorrelationSimilarity(model);
        // ItemSimilarity similarity = new UncenteredCosineSimilarity(model);
        // ItemSimilarity similarity = new LogLikelihoodSimilarity(model);
        GenericItemBasedRecommender recommender = new GenericItemBasedRecommender(model, similarity);

        // TODO: 적절한 code 입력
        // HINT: Use BufferedWriter instance
        BufferedWriter out = new BufferedWriter(new FileWriter("item-based-result.txt"));

        LongPrimitiveIterator items = model.getItemIDs();
        while (items.hasNext()) {
            long itemId = items.nextLong();
            // TODO: 적절한 code 입력
            // HINT: Use if statement + break
            if(itemId<11){
                System.out.println("\nitemId is : " + itemId);
                List<RecommendedItem> recommendations = recommender.mostSimilarItems(itemId, 10);   // FIXME: (itemId, howMany)
                for (RecommendedItem recommendation : recommendations) {
                    String resultRow = itemId + ", " + recommendation.getItemID() +
                            ", " + recommendation.getValue();
                    System.out.println(resultRow);
                    out.write(resultRow);
                    out.newLine();
                }
            }else{
                break;
            }
        }
        out.flush();
        out.close();
    }
}
