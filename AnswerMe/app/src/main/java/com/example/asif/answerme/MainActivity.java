package com.example.asif.answerme;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    TextView textView;
    int chosenAnswer = 0;
    int locationOfCorrect = 0;
    String[] answers = new String[4];
    Button but0;
    Button but1;
    Button but2;
    Button but3;
    ArrayList<String> questions = new ArrayList<>();
    ArrayList<String> options = new ArrayList<>();
    ArrayList<String> correctAnswers = new ArrayList<>();
    ArrayList<String> quest;

    public void generateQuestion(){

        Random rand = new Random();

        locationOfCorrect = rand.nextInt(4);

        int incorrectAnswer;

        for(int i=0; i < 4; i++){

            if(i == locationOfCorrect){



            }

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.question);

        String result = "";

        DownloadTask downloadTask = new DownloadTask();

        try {

            result = downloadTask.execute("https://opentdb.com/api.php?amount=10").get();

            Log.i("Result: ", result);

        } catch (ExecutionException e) {

            e.printStackTrace();

        } catch (InterruptedException e) {

            e.printStackTrace();

        }


    }


    public class DownloadTask extends AsyncTask<String, Void, String> {

        String question = "";

        String correctAnswer = "";

        String incorrectAnswers = "";

        JSONArray incorrect = new JSONArray();

        @Override
        protected String doInBackground(String... strings) {

            String result = "";

            URL url;

            HttpURLConnection urlConnection;

            try{

                url = new URL(strings[0]);

                urlConnection = (HttpURLConnection) url.openConnection();

                InputStream in = urlConnection.getInputStream();

                InputStreamReader reader = new InputStreamReader(in);

                int data = reader.read();

                while (data != -1){

                    char current = (char) data;

                    result += current;

                    data = reader.read();

                }

                return result;


            } catch (MalformedURLException e) {

                e.printStackTrace();

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {

                String message = "";

                JSONObject jsonObject = new JSONObject(s);

                String results = jsonObject.getString("results");

                Log.i("Results of the quiz: ", results);

                JSONArray arr = new JSONArray(results);

                for(int i = 0; i < arr.length(); i++){

                    JSONObject jsonPart = arr.getJSONObject(i);

                    question = jsonPart.getString("question");

                    questions.add(question);

                    correctAnswer = jsonPart.getString("correct_answer");

                    correctAnswers.add(correctAnswer);

                    incorrect = jsonPart.getJSONArray("incorrect_answers");

                    incorrect.put(jsonPart.getString("correct_answer"));

                    options.add(String.valueOf(incorrect));
//
//
//                    incorrectAnswers = jsonPart.getString("incorrect_answers");
//
//                    options.add(correctAnswer);
//
//                    options.add(incorrectAnswers);
//
//                    System.out.println("The Questions are: " + questions);
//
//                    System.out.println("The Options are: " + options);
//
//                    textView.setText(questions.get(i));

                }

                System.out.println("All question: " + questions.get(1));

                System.out.println("All shdkjsa jksd: " + options.get(1));


                System.out.println("Correct shdkjsa jksd: " + correctAnswers.get(1));

                textView.setText(questions.get(0));

                for(int i = 0; i<4: i++){



                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

//    public class Questions {
//
//        quest = new Arr
//
//
//
//        public String choices[][] = {
//                {"HTML", "CSS", "Vala", "PHP"},
//                {"Punction Marks", "Back-Slash", "Brackets", "Semi Colon"},
//                {"Siemens Applications", "Student Applications", "Social Applications", "Commercial Applications"}
//        };
//
//        public String correctAnswer[] = {
//                "PHP",
//                "Brackets",
//                "Commercial Applications"
//        };
//
//        public String getQuestion(int a){
//            String question = questions[a];
//            return question;
//        }
//
//        public String getchoice1(int a){
//            String choice = choices[a][0];
//            return choice;
//        }
//
//        public String getchoice2(int a){
//            String choice = choices[a][1];
//            return choice;
//        }
//
//        public String getchoice3(int a){
//            String choice = choices[a][2];
//            return choice;
//        }
//
//        public String getchoice4(int a){
//            String choice = choices[a][3];
//            return choice;
//        }
//
//        public String getCorrectAnswer(int a){
//            String answer = correctAnswer[a];
//            return answer;
//        }
//
//    }


}


