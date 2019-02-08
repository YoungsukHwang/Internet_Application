package kr.ac.snu.imlab.ia.hw3;

import edu.stanford.nlp.io.IOUtils;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class POSTagger {
    public static void main(String[] args) throws IOException {
        String filepath = "doc/pap.txt";    // 텍스트 문서 파일 경로

        /**
         * 2-3. Tagger를 변경해 가면서 2-1, 2-2 과정을 반복하기
         *      : taggerFilename 문자열 수정
         */
        String taggerFilename =
            "res/taggers/english-bidirectional-distsim.tagger"; // FIXME: 사용할 tagger 파일 경로
            // "res/taggers/english-left3words-distsim.tagger";
            // "res/taggers/wsj-0-18-left3words-nodistsim.tagger";

        MaxentTagger tagger = new MaxentTagger(taggerFilename);

        // POS Tagging 테스트용 문자열:
        String str = "Good afternoon Rajat Raina, how are you today?\n" +
                "I go to school at Stanford University, which is located in California.";

        //TagString(tagger, str); // 테스트용 문자열에 대하여 POS Tagging 수행 실험

        TagFile(tagger, filepath);  // TODO: 텍스트 문서 파일에 대하여 POS Tagging 수행
    }

    /**
     * 2-1. 주어진 tagger를 사용하여 POS Tagging을 수행하고 그 결과를 txt 파일로 출력하기
     * 2-2. 2-1의 수행 결과에 대하여, 각각의 tag가 몇 개 나왔는지 세어 그 결과를 txt 파일로 출력하기
     * 2-3. Tagger를 변경해 가면서 2-1, 2-2 과정을 반복하기
     *      : TagFile() 메서드 수정
     */
    static void TagFile(MaxentTagger tagger, String filepath) throws IOException {
        String content = IOUtils.slurpFile(filepath);   // 텍스트 문서의 내용을 문자열 형태로 변환
        /**
         * (1) 정규 표현식을 사용하여 원본 텍스트로부터 tag 부분만을 캡쳐하고,
         *     각각의 tag가 몇 개 나왔는지 세기
         */
        // TODO: 적절한 code 입력
        // HINT: Use tagString() method to perform tagging
        //       and use HashMap<String, Integer> to keep counting of each tag

        String result = tagger.tagString(content);
        System.out.println(result);

        Pattern pattern = Pattern.compile("\\w+_([A-Z]*)");
        Matcher m = pattern.matcher(result);

        BufferedWriter out = new BufferedWriter(new FileWriter("pap-tag.txt"));
        out.write(result);
        out.flush();
        out.close();

        BufferedWriter count = new BufferedWriter(new FileWriter("pap-tag-count.txt"));

        Map<String, Integer> map = new HashMap<String, Integer>();
        // tag : "CC,CD,DT,EX,FW,IN,JJ,JJR,JJS,LS,MD,NN,NNS,NNP,NNPS,PDT,POS,PRP,PRP$,RB,RBR,RBS,RP,SYM,TO,UH,VB,VBD,VBG,VBN,VBP,VBZ,WDT,WP,WP$,WRB"

        while (m.find()) {
            System.out.println("Found the tag: " + m.group(1));
            if(map.containsKey(m.group(1)))
            {
                int value=map.get(m.group(1));
                ++value;
                map.put(m.group(1),value);
            }else {
                map.put(m.group(1), 1);
            }
        }
        System.out.println(map.toString());
        /**
         * (2) Tag 개수를 센 결과를 txt 파일로 출력하기
         */
        // TODO: 적절한 code 입력
        // HINT: Use BufferedWriter instance
        count.write(map.toString());
        count.flush();
        count.close();
    }

    /**
     * 문자열에 대하여 POS Tagging을 수행하는 메서드 - 과제 수행 시 참조용
     */
    static void TagString(MaxentTagger tagger, String str) {
        String result = tagger.tagString(str);
        System.out.println(result);

        Pattern pattern = Pattern.compile("\\w+_([A-Z]*)");

        Matcher m = pattern.matcher(result);

        while (m.find()) {
            System.out.println("Found the tag: " + m.group(1));
        }
        System.out.println("The example ends");
    }
}
