package com.miraj.loktragithubtask;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.miraj.loktragithubtask.adapter.CommitRVAdapter;
import com.miraj.loktragithubtask.model.Commit;
import com.miraj.loktragithubtask.model.Committer;
import com.miraj.loktragithubtask.util.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private RecyclerView commitsList;
    private SwipeRefreshLayout swipeRefreshLayout;

    private View rootView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rootView=findViewById(android.R.id.content);

        commitsList = (RecyclerView) findViewById(R.id.commitsList);
        commitsList.setLayoutManager(new LinearLayoutManager(this));

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRL);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
            }
        });

        Toast.makeText(this, R.string.snackbar_pull_refresh_message, Toast.LENGTH_SHORT)
                .show();

    }

    @Override
    protected void onResume() {
        super.onResume();

        loadData();

    }

    private void loadData(){
        if(Utils.isNetworkAvailable(this)) {
            Log.e(LOG_TAG,"no internet available");
            new GetCommitsAsyncTask().execute();
        }
        else{
            Log.e(LOG_TAG,"no internet available");
            Snackbar.make(rootView,R.string.no_internet_message,Snackbar.LENGTH_LONG).show();
        }
    }

    private class GetCommitsAsyncTask extends AsyncTask<Void,Void,List<Commit>>{


        @Override
        protected List<Commit> doInBackground(Void... voids) {

            HttpURLConnection urlConnection;
            URL url;
            InputStream inputStream;
            List<Commit> commits = new ArrayList<>();

            try{
                url = new URL("https://api.github.com/repos/rails/rails/commits?per_page=25");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setDoInput(true);
                urlConnection.connect();

                int httpStatus = urlConnection.getResponseCode();

                if (httpStatus != HttpURLConnection.HTTP_OK) {

                    Map<String, List<String>> map = urlConnection.getHeaderFields();
                    for (Map.Entry<String, List<String>> entry : map.entrySet()) {
                        System.out.println(entry.getKey()+ " : " + entry.getValue());
                    }

                    Snackbar.make(rootView,R.string.no_internet_message,Snackbar.LENGTH_LONG).show();

                }
                else {
                    inputStream = urlConnection.getInputStream();


                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    String temp,response="";
                    while((temp = bufferedReader.readLine())!=null){
                        response+=temp;
                    }

                    Log.e(LOG_TAG,response);

                    //Api rate limit
                    if(response.contains("API rate limit exceeded")){

                        Snackbar.make(rootView,R.string.api_limit_exceeded_error,Snackbar.LENGTH_LONG).show();

                    }
                    else {

                        JSONTokener jsonTokener = new JSONTokener(response);
                        JSONArray jsonArray = (JSONArray) jsonTokener.nextValue();
                        for(int i =0; i<jsonArray.length();i++){

                            JSONObject obj = (JSONObject) jsonArray.get(i);
                            Commit commit = new Commit();

                            commit.setSha(obj.getString("sha"));
                            commit.setCommitUrl(obj.getString("html_url"));

                            JSONObject commitObj = obj.getJSONObject("commit");
                            commit.setCommitMessage(commitObj.getString("message"));

                            JSONObject committerObj = obj.getJSONObject("committer");
                            Committer committer = new Committer();

                            committer.setId(committerObj.getLong("id"));
                            committer.setAvatarUrl(committerObj.getString("avatar_url"));
                            committer.setCommitterUrl(committerObj.getString("html_url"));

                            committer.setName(commitObj.getJSONObject("committer").getString("name"));
                            commit.setCommitter(committer);

                            commits.add(commit);

                        }
                    }
                }


                urlConnection.disconnect();

            }catch (ProtocolException e){
                e.printStackTrace();
            }
            catch ( JSONException | IOException e) {
                e.printStackTrace();
            }

            return commits;
        }


        @Override
        protected void onPostExecute(List<Commit> commits) {

            commitsList.setAdapter(new CommitRVAdapter(commits,MainActivity.this));
            swipeRefreshLayout.setRefreshing(false);
        }
    }
}
