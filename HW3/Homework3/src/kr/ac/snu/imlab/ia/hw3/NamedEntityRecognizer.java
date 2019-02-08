package kr.ac.snu.imlab.ia.hw3;

import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.io.IOUtils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NamedEntityRecognizer {
    public static void main(String[] args) throws IOException {
        String filepath = "doc/a-z-cons.txt";   // 텍스트 문서 파일 경로
        /**
         * 1-3. 분류기를 변경해 가면서 1-1, 1-2 과정을 반복하기
         *      : classifierFilename 문자열 수정
         */
        String classifierFilename =
                // "res/classifiers/english.all.3class.distsim.crf.ser.gz";    // FIXME: 사용할 분류기 파일 경로
                // "res/classifiers/english.conll.4class.distsim.crf.ser.gz";
                 "res/classifiers/english.muc.7class.distsim.crf.ser.gz";

        AbstractSequenceClassifier classifier =
                CRFClassifier.getClassifierNoExceptions(classifierFilename);    // 사용할 분류기를 메모리 상에 로드

        // NER 테스트용 문자열:
        String str = "Good afternoon Rajat Raina, how are you today?\n" +
                "I go to school at Stanford University, which is located in California.";
        //NERString(classifier, str); // 테스트용 문자열에 대하여 NER 수행 시험

        NERFile(classifier, filepath);  // TODO: 텍스트 문서 파일에 대하여 NER 수행
    }

    /**
     * 1-1. 주어진 분류기를 사용하여 named entity recognition 수행하고 그 결과를 txt 파일로 출력하기
     * 1-2. 1-1의 수행 결과에 대하여, 각각의 entity가 몇 개 나왔는지 세어 그 결과를 txt 파일로 출력하기
     * 1-3. 분류기를 변경해 가면서 1-1, 1-2 과정을 반복하기
     *    : NERFile() 메서드 수정
     */
    static void NERFile(AbstractSequenceClassifier classifier, String filepath) throws IOException {
        String content = IOUtils.slurpFile(filepath);   // 텍스트 문서의 내용을 문자열 형태로 변환
        /**
         * (1) 정규 표현식을 사용하여 원본 텍스트로부터 entity 부분만을 캡쳐하고,
         *     각각의 entity가 몇 개 나왔는지 세기
         */
        // TODO: 적절한 code 입력
        // HINT: Use classifyWithInlineXML() method to classify entities
        //       and find the matched pattern using Pattern and Matcher instances
        String result = classifier.classifyWithInlineXML(content);
        //System.out.println(result);

        BufferedWriter out = new BufferedWriter(new FileWriter("a-z-cons-ner.txt"));
        out.write(result);
        out.newLine();
        BufferedWriter count = new BufferedWriter(new FileWriter("a-z-cons-ner-count.txt"));
        int organ=0 , person=0, location = 0, misc=0, date=0, money=0, percent=0, time=0;

        Pattern pattern1 = Pattern.compile("<ORGANIZATION.*?>(.+?)</ORGANIZATION>");
        Matcher m1 = pattern1.matcher(result);
        while (m1.find()) {
            System.out.println("Found the Organization: " + m1.group(1));
            organ++;
            out.write("Found the Organization: " + m1.group(1)+"\n");
        }

        Pattern pattern2 = Pattern.compile("<PERSON.*?>(.+?)</PERSON>");
        Matcher m2 = pattern2.matcher(result);
        while (m2.find()) {
            System.out.println("Found the Person: " + m2.group(1));
            person++;
            out.write("Found the Person: " + m2.group(1)+"\n");
        }

        Pattern pattern3 = Pattern.compile("<LOCATION.*?>(.+?)</LOCATION>");
        Matcher m3 = pattern3.matcher(result);
        while (m3.find()) {
            System.out.println("Found the Location: " + m3.group(1));
            location++;
            out.write("Found the Location: " + m3.group(1)+"\n");
        }

        Pattern pattern4 = Pattern.compile("<DATE.*?>(.+?)</DATE>");
        Matcher m4 = pattern4.matcher(result);
        while (m4.find()) {
            System.out.println("Found the DATE: " + m4.group(1));
            date++;
            out.write("Found the DATE: " + m4.group(1)+"\n");
        }

        Pattern pattern5 = Pattern.compile("<MONEY.*?>(.+?)</MONEY>");
        Matcher m5 = pattern5.matcher(result);
        while (m5.find()) {
            System.out.println("Found the MONEY: " + m5.group(1));
            money++;
            out.write("Found the MONEY: " + m5.group(1)+"\n");
        }

        Pattern pattern6 = Pattern.compile("<PERCENT.*?>(.+?)</PERCENT>");
        Matcher m6 = pattern6.matcher(result);
        while (m6.find()) {
            System.out.println("Found the PERCENT: " + m6.group(1));
            percent++;
            out.write("Found the PERCENT: " + m6.group(1)+"\n");
        }

        Pattern pattern7 = Pattern.compile("<TIME.*?>(.+?)</TIME>");
        Matcher m7 = pattern7.matcher(result);
        while (m7.find()) {
            System.out.println("Found the TIME: " + m7.group(1));
            time++;
            out.write("Found the TIME: " + m7.group(1)+"\n");
        }

        count.write("This is the total counts\n");
        count.write(" ORGANIZATION : " + organ);
        count.write(" PERSON : " + person);
        count.write(" LOCATION : " + location);
        count.write(" DATE : " + date);
        count.write(" MONEY : " + money);
        count.write(" PERCENT : " + percent);
        count.write(" TIME : " + time);
        count.flush();
        count.close();

        out.flush();
        out.close();
        /**
         * (2) Entity 개수를 센 결과를 txt 파일로 출력하기
         */
        // TODO: 적절한 code 입력
        // HINT: Use BufferedWriter instance

        System.out.println("\nThe real thing ends\n");
    }

    /**
     * 문자열에 대하여 NER을 수행하는 메서드 - 과제 수행 시 참조용
     */
    static void NERString(AbstractSequenceClassifier classifier, String str) {
        String result = classifier.classifyWithInlineXML(str);
        // String result = classifier.classifyToString(str);
        // String result = classifier.classifyToString(str, "xml", true);
        System.out.println(result);

        Pattern pattern = Pattern.compile("<ORGANIZATION.*?>(.+?)</ORGANIZATION>");
        Matcher m = pattern.matcher(result);

        while (m.find()) {
            System.out.println("Found the text: " + m.group(1));
        }
        System.out.println("\nThe example ends\n");
    }
}
