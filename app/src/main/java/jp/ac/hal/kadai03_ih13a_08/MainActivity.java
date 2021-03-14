package jp.ac.hal.kadai03_ih13a_08;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.icu.text.DecimalFormat;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

import static java.lang.String.format;

public class MainActivity extends AppCompatActivity {

    //    表示用TextView
    private TextView tvDisp;
    private TextView tvKatei;
//    double moto = 0;
//    double ire = 0;
//    可変長連続計算用の数字配列
    ArrayList<String> list = new ArrayList<>();
//    可変長連続計算用の記号配列
    ArrayList<String> kigoulist = new ArrayList<>();
//    足し算引き算後回し用数字の配列
    ArrayList<String> adsulist = new ArrayList<>();
//    足し算引き算後回し用記号の配列
    ArrayList<String> adsukigoulist = new ArrayList<>();
    String kigou = "";
//    ドット押下後のバック・数字入力対応のためのカウント
    int dot_cnt = 0;
//    数字用フラグ
    boolean flg = false;
//    計算過程用フラグ
    boolean equal_flg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        表示領域のインスタンス化
        tvDisp = findViewById(R.id.display);
        tvKatei = findViewById(R.id.textView3);

//        ボタンのインスタンス化
//        数字ボタンの配列を作る
        Button[] numBtn = new Button[10];
        numBtn[0] = findViewById(R.id.btn0);
        numBtn[1] = findViewById(R.id.btn1);
        numBtn[2] = findViewById(R.id.btn2);
        numBtn[3] = findViewById(R.id.btn3);
        numBtn[4] = findViewById(R.id.btn4);
        numBtn[5] = findViewById(R.id.btn5);
        numBtn[6] = findViewById(R.id.btn6);
        numBtn[7] = findViewById(R.id.btn7);
        numBtn[8] = findViewById(R.id.btn8);
        numBtn[9] = findViewById(R.id.btn9);

        for (int i = 0; i < numBtn.length; i++)
        {
            numBtn[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                  押された部品(View v)を渡して
//                  表示メソッドを呼び出す
                    numBtnClicked(v);
                }
            });
        }
//        クリア
        findViewById(R.id.clear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//              押された部品(View v)を渡して
//              表示メソッドを呼び出す
                init();
            }
        });

//        計算
//        加算
        findViewById(R.id.plus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//              押された部品(View v)を渡して
//              表示メソッドを呼び出す
                calculation(v);
            }
        });
//      減算
        findViewById(R.id.minus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//              押された部品(View v)を渡して
//              表示メソッドを呼び出す
                calculation(v);
            }
        });
//      乗算
        findViewById(R.id.multi).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//              押された部品(View v)を渡して
//              表示メソッドを呼び出す
                calculation(v);
            }
        });
//      除算
        findViewById(R.id.divided).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//              押された部品(View v)を渡して
//              表示メソッドを呼び出す
                calculation(v);
            }
        });
//      プラスマイナス
        findViewById(R.id.plus_minus).setOnClickListener(new View.OnClickListener() {
            @SuppressLint("DefaultLocale")
            @Override
            public void onClick(View v) {
//              押された部品(View v)を渡して
                double num = Double.parseDouble(tvDisp.getText().toString().replace(",", "")) * (-1);
                tvDisp.setText(String.valueOf(num));
                if (num == (int)num)
                {
                    tvDisp.setText(format("%,d",(int)num));
                }
            }
        });
//      ドット
        findViewById(R.id.dot).setOnClickListener(new View.OnClickListener() {
            @SuppressLint("DefaultLocale")
            @Override
            public void onClick(View v) {
//          押された部品(View v)を渡して
            if (flg)
            {
                tvDisp.setText("");
                flg = false;
            }

            if(tvDisp.getText().equals(""))
            {
                tvDisp.setText("0");
            }

            String str = tvDisp.getText().toString().replace(",", "");

            if(!str.contains("."))
            {
                str += ".";
                tvDisp.setText(str);
            }
            }
        });
//      バック
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @SuppressLint("DefaultLocale")
            @Override
            public void onClick(View v) {
//              押された部品(View v)を渡し
                back();
            }
        });
//      計算結果
        findViewById(R.id.equal).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//              押された部品(View v)を渡して
//              表示メソッドを呼び出す
                kekka();
            }
        });
    }
//    初期化・クリア
    private void init(){
//        表示のリセット
        tvDisp.setText("0");
        tvKatei.setText("計算過程");
//        ドットカウントのリセット
        dot_cnt = 0;
//        リストしセット
        kigoulist.clear();
        list.clear();
        adsukigoulist.clear();
        adsulist.clear();
    }
//    計算
    @SuppressLint("SetTextI18n")
    private  void calculation(View v){

        Button btn = (Button) v;
        kigou = btn.getText().toString();
//        数値と記号を配列に格納
        list.add(tvDisp.getText().toString().replace(",", ""));
        kigoulist.add(kigou);
        BigDecimal val1 = BigDecimal.valueOf(Double.parseDouble(tvDisp.getText().toString().replace(",", "")));

        if (equal_flg)
        {
            tvKatei.setText("計算過程");
            equal_flg = false;
        }
//        0.0防止
        if (tvKatei.getText().toString().equals("計算過程"))
        {
            if (Double.parseDouble(tvDisp.getText().toString().replace(",",""))== 0)
            {
                tvKatei.setText("0" + kigou );
            }
            else
            {
                tvKatei.setText(val1.stripTrailingZeros().toPlainString() + kigou);
            }
        }
        else
        {
            if (Double.parseDouble(tvDisp.getText().toString().replace(",",""))== 0)
            {
                tvKatei.setText(tvKatei.getText().toString()+ "0" + kigou );
            }
            else
            {
                tvKatei.setText(tvKatei.getText().toString()+ val1.stripTrailingZeros().toPlainString() + kigou );
            }
        }
        tvDisp.setText("");
        dot_cnt = 0;
    }

    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    private void kekka()
    {
        list.add(tvDisp.getText().toString().replace(",", ""));
        BigDecimal val1 = null;
        BigDecimal val2 = null;
//        0.0防止
        if (tvKatei.getText().equals("計算過程")||equal_flg)
        {
            if (Double.parseDouble(tvDisp.getText().toString().replace(",",""))== 0)
            {
                tvKatei.setText("0=");
            }
            else
            {
                tvKatei.setText(BigDecimal.valueOf(Double.parseDouble(tvDisp.getText().toString().replace(",",""))).stripTrailingZeros().toPlainString() + "=" );
            }
        }
        else
        {
            if (Double.parseDouble(tvDisp.getText().toString().replace(",",""))== 0)
            {
                tvKatei.setText(tvKatei.getText().toString()+"0=");
            }
            else
            {
                tvKatei.setText(tvKatei.getText().toString()+ BigDecimal.valueOf(Double.parseDouble(tvDisp.getText().toString().replace(",",""))).stripTrailingZeros().toPlainString() + "=" );
            }
        }

//          記号分岐
//        ×・/優先
        val1 = BigDecimal.valueOf(Double.parseDouble(list.get(0)));
        for (int i=0; i<kigoulist.size();i++)
        {
            val2 = BigDecimal.valueOf(Double.parseDouble(list.get(i+1)));
            if (kigoulist.get(i).equals("+"))
            {
//                val1 = val1.add(val2);
                adsulist.add(val1.toString().replace(",", ""));
                adsukigoulist.add(kigoulist.get(i));
                val1 = val2;
            }
            else if(kigoulist.get(i).equals("-"))
            {
//                val1 = val1.subtract(val2);
                adsulist.add(val1.toString().replace(",", ""));
                adsukigoulist.add(kigoulist.get(i));
                val1 = val2;
            }
            else if(kigoulist.get(i).equals("×"))
            {
                val1 = val1.multiply(val2);
            }
            else if(kigoulist.get(i).equals("/"))
            {
//            12桁まで有効の切り捨て
                if (!val2.toPlainString().equals("0.0"))
                {
                    val1 = val1.divide(val2, 12, RoundingMode.DOWN);
                }
            }
        }
        adsulist.add(val1.toString().replace(",", ""));
//        後回しにした+・-
        val1 = BigDecimal.valueOf(Double.parseDouble(adsulist.get(0)));
        for (int i=0; i<adsukigoulist.size();i++)
        {
            val2 = BigDecimal.valueOf(Double.parseDouble(adsulist.get(i+1)));
            if (adsukigoulist.get(i).equals("+"))
            {
                val1 = val1.add(val2);
            }
            else if(adsukigoulist.get(i).equals("-"))
            {
                val1 = val1.subtract(val2);
            }
        }

        assert val1 != null;
//        余分な.0x00の防止stripTrailingZeros()
        tvDisp.setText(val1.stripTrailingZeros().toPlainString());

        double num = Double.parseDouble(tvDisp.getText().toString());
//        不要な.0消去・カンマ区切り%,d
        if (num == (int)num)
        {
            tvDisp.setText(format("%,d",(int)num));
        }
        flg = true;
        equal_flg = true;
//        リセット
        dot_cnt = 0;
        kigoulist.clear();
        list.clear();
        adsukigoulist.clear();
        adsulist.clear();
    }
//  バック
    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    private void back(){
        String str    = tvDisp.getText().toString();
        int length = str.length();
        if(tvDisp.getText().toString().contains("."))
        {
            dot_cnt-=1;
        }

        if(length > 1 )
        {
            str = str.substring(0, length-1);
            tvDisp.setText(str);
            length = str.length();
//            -で落ちるのを防止
            if(tvDisp.getText().toString().equals("-") && length == 1)
            {
                tvDisp.setText("0");
            }
            else
            {
                double dnum = Double.parseDouble(tvDisp.getText().toString().replace(",",""));
                if (dnum != 0)
                {
                    if (dnum == (int)dnum)
                    {
                        tvDisp.setText(format("%,d",(int)dnum));
                    }
                }
            }
        }
        else
        {
            tvDisp.setText("0");
        }
    }

    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    private void numBtnClicked(View v){
//        押されたボタンに該当する表示処理を記述する
//        Viewの種類をButtonに特定(cast)

        Button btn = (Button) v;
//        押された数字を表示する
//        ->ボタンに表示されているテキストをそのまま表示すれば良い
//        ->入力済みの内容に連結する
//        String str = btn.getText().toString() + btn.getText().toString();
        double num = 0;
//        イコールのあとのエラー防ぐため
        if (flg)
        {
            tvDisp.setText("");
            flg = false;
        }
//          空白に0入れてから計算
        if(tvDisp.getText().equals(""))
        {
            tvDisp.setText("0");
        }
//        replaceで区切った","消去
        num = Double.parseDouble(tvDisp.getText().toString().replace(",", ""));
//          少数の場合
        if(tvDisp.getText().toString().contains("."))
        {
            int waru = 10;
//              少数の桁数に合わせる
            for(int i=0; i<dot_cnt;i++)
            {
                waru *= 10;
            }
            BigDecimal val1 = BigDecimal.valueOf(num);
            BigDecimal val2 = BigDecimal.valueOf(Double.parseDouble(btn.getText().toString())/waru);
            BigDecimal calc = null;
//            ±プラスマイナス
            if (num>=0)
            {
                calc = val1.add(val2);
            }
            else
            {
                calc = val1.subtract(val2);
            }
//            .のあと0が表示されないのを防止する
            if (btn.getText().toString().equals("0"))
            {
                tvDisp.setText(tvDisp.getText().toString()+"0");
            }
            else
            {
                tvDisp.setText(calc.stripTrailingZeros().toPlainString());
            }
            dot_cnt+=1;
        }
        else
        {
//          ±プラスマイナス
            if (num>=0)
            {
                num = num * 10 + Double.parseDouble(btn.getText().toString());
            }
            else
            {
                num = num * 10 - Double.parseDouble(btn.getText().toString());
            }
            tvDisp.setText(format("%,d",(int)num));
        }
    }
}
