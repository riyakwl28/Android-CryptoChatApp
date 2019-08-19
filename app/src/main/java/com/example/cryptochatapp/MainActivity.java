package com.example.cryptochatapp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;

import org.json.JSONObject;

import java.io.InputStream;

public class MainActivity extends AppCompatActivity {


    EditText input;
    TextView output;
    Button submit;
    private String data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        input = (EditText)findViewById(R.id.enc);
        output = (TextView)findViewById(R.id.dec);
        submit = (Button)findViewById(R.id.submit);

        data=input.getText().toString();
        new HttpAsyncTask().execute("http://192.168.43.138:8000/predicts",data);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // call AsynTask to perform network operation on separate thread

            }
        });

    }

    public static String POST(String url, String data){
        String result = "";
        InputStream inputStream = null;


        try {
            Log.e("post","method");
            // 1. create HttpClient
            HttpClient httpclient = new DefaultHttpClient();
            Log.e("post","before");
            // 2. make POST request to the given URL
            HttpPost httpPost = new HttpPost(url);
            Log.e("post","after");
            String json = "";

            Log.e("data in post",data);

            // 3. build jsonObject
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("name",data);

            // 4. convert JSONObject to JSON to String
            json = jsonObject.toString();

            // ** Alternative way to convert Person object to JSON string usin Jackson Lib
            // ObjectMapper mapper = new ObjectMapper();
            // json = mapper.writeValueAsString(person);
            Log.e("json",json);
            // 5. set json to StringEntity
            StringEntity se = new StringEntity(json);

            // 6. set httpPost Entity
            httpPost.setEntity(se);
            Log.e("execute","before");
            // 8. Execute POST request to the given URL
            HttpResponse httpResponse = httpclient.execute(httpPost);
            Log.e("execute","after");
            // 9. receive response as inputStream
            //inputStream = httpResponse.getEntity().getContent();

            HttpEntity entity = httpResponse.getEntity();
            String responseString = EntityUtils.toString(entity, "UTF-8");
            Log.e("response",responseString);
            // 10. convert inputstream to string
            if(responseString != null){
                Log.e("result","notNull");
                result = responseString;}
            else{
                Log.e("result","null");
                result = "Did not work!";}


        }
        catch (Exception e){

        }
        return result;
    }


    private class HttpAsyncTask extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... urls) {
            Log.e("data",urls[1]);
            return POST(urls[0],urls[1]);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            Log.d("result",result);
            Toast.makeText(getBaseContext(), result, Toast.LENGTH_LONG).show();
            try {
                JSONObject obj = new JSONObject(result);
                String  reslt = obj.getString("Encryption");
                output.setText(reslt);
                Log.e("output","after");
            } catch (Throwable t){

            }
        }
    }
}
