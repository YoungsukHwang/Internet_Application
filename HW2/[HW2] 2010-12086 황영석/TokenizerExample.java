
package kr.ac.snu.imlab.ia.hw2.tokenizer;

import org.galagosearch.core.parse.Document;
import org.galagosearch.core.parse.TagTokenizer;
import org.galagosearch.tupleflow.InputClass;
import org.galagosearch.tupleflow.OutputClass;
import org.galagosearch.tupleflow.execution.Verified;

@Verified
@InputClass(className = "org.galagosearch.core.parse.Document")
@OutputClass(className = "org.galagosearch.core.parse.Document")
public class TokenizerExample extends TagTokenizer {

    /**
     * 1-1. 따옴표로 시작해서 따옴표로 끝나는 단어의 따옴표 없애기,
     *      단어 도중에 따옴표가 나오는 경우, 따옴표를 포함한 뒤의 글자 모두 삭제하기
     *      : tokenSimpleFix() 메서드 수정
     */
    @Override
    protected String tokenSimpleFix(String token) {
        char[] chars = token.toCharArray();
        int j = 0;
        //1번과제
        // TODO: 적절한 code 입력
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            boolean isApostrophe = (c=='\'');
            boolean isApostropheOnTheFirst = (chars[0]=='\'');

            // TODO: 적절한 code 입력
            if(isApostrophe){ //만약 임의의 따옴표를 발견했다면
                if(isApostropheOnTheFirst){ //따옴표가 토큰 첫머리에 있는 따옴표냐?
                    //그럼 Do nothing
                }else{//안 그러면 중간머리에 있는 따옴표고
                    break; //그 뒤는 다 없애버릴 것이므로 루프를 벗어나자
                }
                j--; //따옴표면 문자 복붙을 안해야하니까
            }else{
                chars[j] = chars[i]; //따옴표를 뺀 문자를 복붙합시다
            }
            j++;
        }
        // Find JavaDoc and understand the behavior of String(char[], int, int) constructor.
        // TODO: 적절한 code 입력
        token = new String(chars, 0, j);
        
        return token;
    }

    /**
     * 1-2. ".com"으로 끝나는 단어는 토큰화되지 않도록 수정하기
     * 1-3. 마침표(.)로 연결된 단어에서, 마침표 앞, 뒤, 또는 사이에 있는 글자가 모두 1개인 경우 마침표를 삭제하고,
     *      2개 이상인 경우 토큰화되지 않도록 수정하기
     *      : tokenAcronymProcessing() 메서드 수정
     */
    @Override
    protected void tokenAcronymProcessing(String token, int start, int end) {
        token = tokenComplexFix(token);
     
        // remove start and ending periods
        while (token.startsWith(".")) {
            token = token.substring(1);
            start = start + 1;
        }

        while (token.endsWith(".")) {
            token = token.substring(0, token.length() - 1);
            end -= 1;
        }

        // TODO: 적절한 code 입력
        // HINT: use if + return statement
        if (token.endsWith(".com")) { //먄약 토큰이 .com으로 끝난다면
            addToken(token, start, end); //그 토큰은 dot(.)이 문자열 중간에 붙어있지만 토큰으로 인정하자
            return;
        }

        // does the token have any periods left?
        if (token.indexOf('.') >= 0) {

            // is this an acronym?  then there will be periods
            // at odd positions:
            boolean isAcronym = token.length() > 0;

            for (int pos = 1; pos < token.length(); pos += 2) {
                if (token.charAt(pos) != '.') {
                    isAcronym = false;
                }
            }

            if (isAcronym) {
            	token = token.replace(".", "");
                addToken(token, start, end);
            } else {
                // TODO: 적절한 code 입력
                // HINT: use String.split() method
                //       and addToken(token, start, end) statement;
                boolean moreThan2=false; //moreThan2라는 boolean을 정의.
                                        // periods 사이에 문자가 2자 이상이면 true, 아니면 false. default는 false로 설정
                String [] tokens;
                tokens = token.split("[.]",-1); //구둣점을 delim으로 토큰을 스플릿하자. 그리고 tokens라는 스트링의 어레이에 넣자

                for(int i=0; i < tokens.length ; i++){
                    if(tokens[i].length() > 1) { //만약 tokens의 각 원소(스트링)가 2자 이상이면 moreThan2는 true다
                        moreThan2 = true;
                    }
                }

                if(moreThan2){ //periods 사이의 문자가 2자 이상인 게 있다면
                    addToken(token, start, end); //그 토큰은 분리하지 말고 그냥 하나의 큰 토큰으로 인정하자 i.e. 300.194.38.9
                    return;
                }

            	int s = 0;
        		for (int e = 0; e < token.length(); e++) {
        			if (token.charAt(e) == '.') {
        				if (e - s > 1) {
        					String subtoken = token.substring(s, e);
        					addToken(subtoken, start + s, start + e);
        				}
                        s = e + 1;
        			}
        		}

        		if (token.length() - s > 1) {
        			String subtoken = token.substring(s);
        			addToken(subtoken, start + s, end);
        		}
            }
        } else {
            addToken(token, start, end);
        }
    }
    
    public static void main(String[] args) throws Exception {
        String originalString = "i've 'hello' 'hello'world' imlab's PH.D I.B.M snu.ac.kr 127.0.0.1 ieee.803.99 naver.com gigabyte.tw";
        System.out.println(originalString);

        TagTokenizer tt = new TagTokenizer();
        Document tr = tt.tokenize(originalString);

        StringBuilder sb1 = new StringBuilder();
        for (int index = 0; index < tr.terms.size(); index++) {
            sb1.append(tr.terms.get(index));
            sb1.append(" ");
        }
        System.out.println(sb1.toString());

        TokenizerExample tn = new TokenizerExample();
        Document tokenizedResult = tn.tokenize(originalString);

        StringBuilder sb2 = new StringBuilder();
        for (int index = 0; index < tokenizedResult.terms.size(); index++) {
            sb2.append(tokenizedResult.terms.get(index));
            sb2.append(" ");
        }
        System.out.println(sb2.toString());
    }
}
