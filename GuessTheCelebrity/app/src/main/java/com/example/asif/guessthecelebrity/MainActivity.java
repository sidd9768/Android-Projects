package com.example.asif.guessthecelebrity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> imageURL = new ArrayList<String>();
    ArrayList<String> celebNames = new ArrayList<String>();
    int chosenCeleb=0;
    ImageView imageView;
    int locationOfCorrect = 0;
    String[] answers = new String[4];
    Button but0;
    Button but1;
    Button but2;
    Button but3;

    public void newQuestion(){

        Random rand = new Random();
        chosenCeleb = rand.nextInt(imageURL.size());

        ImageDownloader imageTask = new ImageDownloader();

        Bitmap celebImage;

        try {

            celebImage = imageTask.execute(imageURL.get(chosenCeleb)).get();

            imageView.setImageBitmap(celebImage);

            locationOfCorrect = rand.nextInt(4);

            int incorrectAnswers;

            for(int i =0; i < 4; i++){

                if(i == locationOfCorrect){

                    answers[i] = celebNames.get(chosenCeleb);

                }else{

                    incorrectAnswers = rand.nextInt(imageURL.size());

                    while (incorrectAnswers == chosenCeleb){

                        incorrectAnswers = rand.nextInt(imageURL.size());

                    }
                    answers[i] = celebNames.get(incorrectAnswers);

                }

            }

            but0.setText(answers[0]);
            but1.setText(answers[1]);
            but2.setText(answers[2]);
            but3.setText(answers[3]);



        } catch (Exception e) {

            e.printStackTrace();

        }

    }


    public void celebChosen (View view){

        if(view.getTag().toString().equals(Integer.toString(locationOfCorrect))){

            Toast.makeText(getApplicationContext(), "Correct", Toast.LENGTH_LONG).show();

        }else{

            Toast.makeText(getApplicationContext(), "Incorrect! It was: " + celebNames.get(chosenCeleb), Toast.LENGTH_SHORT).show();

        }

        newQuestion();

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TaskDownloader taskDownloader = new TaskDownloader();
        imageView = (ImageView) findViewById(R.id.imageView);
        but0 = (Button) findViewById(R.id.but0);
        but1 = (Button) findViewById(R.id.but1);
        but2 = (Button) findViewById(R.id.but2);
        but3 = (Button) findViewById(R.id.but3);


        String result="";

        try {

            result = taskDownloader.execute("http://www.posh24.se/kandisar").get();

            String[] resultSplit = result.split("<div class=\"sidebarContainer\">");

            Pattern p = Pattern.compile("img src=\"(.*?)\"");

            Matcher m = p.matcher(resultSplit[0]);

            while (m.find()){

                imageURL.add(m.group(1));

            }

            p = Pattern.compile("alt=\"(.*?)\"");

            m = p.matcher(resultSplit[0]);

            while (m.find()){
                celebNames.add(m.group(1));
            }

            newQuestion();


        } catch (ExecutionException e) {

            e.printStackTrace();

        } catch (InterruptedException e) {

            e.printStackTrace();

        }



    }
}
