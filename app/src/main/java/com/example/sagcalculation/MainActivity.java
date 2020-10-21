package com.example.sagcalculation;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void onButtonClick(View v) {

        EditText surfaceInput = (EditText) findViewById(R.id.conductorSectionSurfaceInput);
        EditText diameterInput = (EditText) findViewById(R.id.conductorDiameterInput);
        EditText massInput = (EditText) findViewById(R.id.massInput);
        EditText malleabilityFactorInput = (EditText) findViewById(R.id.malleabilityFactorInput);
        EditText excessWeightFactorInput = (EditText) findViewById(R.id.excessWeightFactorInput);
        EditText elasticityModuleInput = (EditText) findViewById(R.id.elasticityModuleInput);
        EditText normalStrainInput = (EditText) findViewById(R.id.normalStrainInput);
        EditText excessStrainInput = (EditText) findViewById(R.id.excessStrainInput);
        EditText maxStrainInput = (EditText) findViewById(R.id.maxStrainInput);
        EditText poleSpanInput = (EditText) findViewById(R.id.poleSpanInput);
        EditText poleDenivelationInput = (EditText) findViewById(R.id.poleDenivelationInput);

        if (surfaceInput.getText().toString().equals("")
                || diameterInput.getText().toString().equals("")
                || massInput.getText().toString().equals("")
                || malleabilityFactorInput.getText().toString().equals("")
                || excessWeightFactorInput.getText().toString().equals("")
                || elasticityModuleInput.getText().toString().equals("")
                || normalStrainInput.getText().toString().equals("")
                || excessStrainInput.getText().toString().equals("")
                || maxStrainInput.getText().toString().equals("")
                || poleSpanInput.getText().toString().equals("")
                || poleDenivelationInput.getText().toString().equals("")) {
            Toast.makeText(MainActivity.this, "Potrebno je ispuniti sva polja",
                    Toast.LENGTH_SHORT).show();
        } else {

            Double surface = Double.parseDouble(surfaceInput.getText().toString());
            Double diameter = Double.parseDouble(diameterInput.getText().toString());
            Double mass = Double.parseDouble(massInput.getText().toString());
            Double malleabilityFactor = Double.parseDouble(malleabilityFactorInput.getText().toString());
            Double excessWeightFactor = Double.parseDouble(excessWeightFactorInput.getText().toString());
            Double elasticityModule = Double.parseDouble(elasticityModuleInput.getText().toString());
            Double normalStrain = Double.parseDouble(normalStrainInput.getText().toString());
            Double excessStrain = Double.parseDouble(excessStrainInput.getText().toString());
            Double maxStrain = Double.parseDouble(maxStrainInput.getText().toString());
            Double poleSpan = Double.parseDouble(poleSpanInput.getText().toString());
            Double poleDenivelation = Double.parseDouble(poleDenivelationInput.getText().toString());

            if (surface == 0
                    || diameter == 0
                    || mass == 0
                    || malleabilityFactor == 0
                    || excessWeightFactor == 0
                    || elasticityModule == 0
                    || normalStrain == 0
                    || excessStrain == 0
                    || maxStrain == 0
                    || poleSpan == 0) {
                Toast.makeText(MainActivity.this, "Vrijednosti moraju biti veće od 0",
                        Toast.LENGTH_SHORT).show();
            } else {

                Double directDistance = Math.sqrt(Math.pow(poleDenivelation, 2) + Math.pow(poleSpan, 2));

                Double reducedWeight = mass * 9.81 / surface;

                Double reducedWeightOfIce = (excessWeightFactor * 0.18 * Math.sqrt(diameter) * 9.81) / surface;

                Double reducedConductorWeight = reducedWeight + reducedWeightOfIce;

                Double criticalSpan = maxStrain * Math.sqrt((360 * malleabilityFactor)
                        / (Math.pow(reducedConductorWeight, 2) - Math.pow(reducedWeight, 2)));

                Double idealSpan = Math.sqrt((Math.pow(poleSpan, 3) / (Math.pow(directDistance, 2) / poleSpan)))
                        * ((Math.pow(directDistance, 3) / Math.pow(poleSpan, 2)) / (Math.pow(directDistance, 2) / poleSpan));

                Double strain = maxStrain * ((Math.pow(directDistance, 3) / Math.pow(poleSpan, 2)) / (Math.pow(directDistance, 2) / poleSpan));


                if (idealSpan > criticalSpan) {
                    Double temperature = -5.0;

                    Double secCo1 = -(strain - (((Math.pow(idealSpan, 2) / 24) * (Math.pow(reducedConductorWeight, 2)
                            / Math.pow(strain, 2)) - (malleabilityFactor * (temperature - (-20)))) * elasticityModule));
                    Double secCo2 = -(strain - (((Math.pow(idealSpan, 2) / 24) * (Math.pow(reducedConductorWeight, 2)
                            / Math.pow(strain, 2)) - (malleabilityFactor * (temperature - (-5)))) * elasticityModule));
                    Double secCo3 = -(strain - (((Math.pow(idealSpan, 2) / 24) * (Math.pow(reducedConductorWeight, 2)
                            / Math.pow(strain, 2)) - (malleabilityFactor * (temperature - 40))) * elasticityModule));

                    Double thirdCo1 = -((Math.pow(idealSpan, 2) * Math.pow(reducedWeight, 2)) / 24) * elasticityModule;
                    Double thirdCo2 = -((Math.pow(idealSpan, 2) * Math.pow(reducedConductorWeight, 2)) / 24) * elasticityModule;
                    Double thirdCo3 = -((Math.pow(idealSpan, 2) * Math.pow(reducedWeight, 2)) / 24) * elasticityModule;

                    Cubic cub = new Cubic();
                    cub.solve(1.0, secCo1, 0.0, thirdCo1);
                    Double strainRaw1 = cub.x1;

                    Cubic cub2 = new Cubic();
                    cub2.solve(1.0, secCo2, 0.0, thirdCo2);
                    Double strainRaw2 = cub2.x1;

                    Cubic cub3 = new Cubic();
                    cub3.solve(1.0, secCo3, 0.0, thirdCo3);
                    Double strainRaw3 = cub3.x1;


                    Double strain20 = strainRaw1 * ((Math.pow(directDistance, 2) / idealSpan) / ((Math.pow(directDistance, 3) / Math.pow(idealSpan, 2))));
                    Double provjes20 = ((Math.pow(idealSpan, 2) * reducedWeight) / (8 * strain20)) * (directDistance / idealSpan);


                    Double strain5 = strainRaw2 * ((Math.pow(directDistance, 2) / idealSpan) / ((Math.pow(directDistance, 3) / Math.pow(idealSpan, 2))));
                    Double provjes5 = ((Math.pow(idealSpan, 2) * reducedConductorWeight) / (8 * strain5)) * (directDistance / idealSpan);


                    Double strain40 = strainRaw3 * ((Math.pow(directDistance, 2) / idealSpan) / ((Math.pow(directDistance, 3) / Math.pow(idealSpan, 2))));
                    Double provjes40 = ((Math.pow(idealSpan, 2) * reducedWeight) / (8 * strain40)) * (directDistance / idealSpan);

                    Intent intent = new Intent(MainActivity.this, Result.class);
                    Bundle a = new Bundle();
                    a.putDouble("provjes", provjes20);
                    a.putDouble("provjes1", provjes5);
                    a.putDouble("provjes2", provjes40);
                    intent.putExtras(a);
                    startActivity(intent);


                } else {
                    Double temperature = -20.0;

                    Double secCo1 = -(strain - (((Math.pow(idealSpan, 2) / 24) * (Math.pow(reducedConductorWeight, 2) / Math.pow(strain, 2)) - (malleabilityFactor * (temperature - (-20)))) * elasticityModule));
                    Double secCo2 = -(strain - (((Math.pow(idealSpan, 2) / 24) * (Math.pow(reducedConductorWeight, 2) / Math.pow(strain, 2)) - (malleabilityFactor * (temperature - (-5)))) * elasticityModule));
                    Double secCo3 = -(strain - (((Math.pow(idealSpan, 2) / 24) * (Math.pow(reducedConductorWeight, 2) / Math.pow(strain, 2)) - (malleabilityFactor * (temperature - 40))) * elasticityModule));

                    Double thirdCo1 = -((Math.pow(idealSpan, 2) * Math.pow(reducedWeight, 2)) / 24) * elasticityModule;
                    Double thirdCo2 = -((Math.pow(idealSpan, 2) * Math.pow(reducedConductorWeight, 2)) / 24) * elasticityModule;
                    Double thirdCo3 = -((Math.pow(idealSpan, 2) * Math.pow(reducedWeight, 2)) / 24) * elasticityModule;

                    Cubic cub = new Cubic();
                    cub.solve(1.0, secCo1, 0.0, thirdCo1);
                    Double strainRaw1 = cub.x1;

                    Cubic cub2 = new Cubic();
                    cub2.solve(1.0, secCo2, 0.0, thirdCo2);
                    Double strainRaw2 = cub2.x1;

                    Cubic cub3 = new Cubic();
                    cub3.solve(1.0, secCo3, 0.0, thirdCo3);
                    Double strainRaw3 = cub3.x1;


                    Double strain20 = strainRaw1 * ((Math.pow(directDistance, 2) / idealSpan) / ((Math.pow(directDistance, 3) / Math.pow(idealSpan, 2))));
                    Double provjes20 = ((Math.pow(idealSpan, 2) * reducedWeight) / (8 * strain20)) * (directDistance / idealSpan);


                    Double strain5 = strainRaw2 * ((Math.pow(directDistance, 2) / idealSpan) / ((Math.pow(directDistance, 3) / Math.pow(idealSpan, 2))));
                    Double provjes5 = ((Math.pow(idealSpan, 2) * reducedConductorWeight) / (8 * strain5)) * (directDistance / idealSpan);


                    Double strain40 = strainRaw3 * ((Math.pow(directDistance, 2) / idealSpan) / ((Math.pow(directDistance, 3) / Math.pow(idealSpan, 2))));
                    Double provjes40 = ((Math.pow(idealSpan, 2) * reducedWeight) / (8 * strain40)) * (directDistance / idealSpan);

                    Intent intent = new Intent(MainActivity.this, Result.class);
                    Bundle a = new Bundle();
                    a.putDouble("provjes", provjes20);
                    a.putDouble("provjes1", provjes5);
                    a.putDouble("provjes2", provjes40);
                    intent.putExtras(a);
                    startActivity(intent);

                }
            }
        }

    }
}

/*

 //Double provjesN = ((poleSpan1-b1)*b1*reducedWeight)/(2*stvNap40);

 //t1.setText(Double.toString(strain));

                //Double temperature1 = -20.0;
                //Double temperature2 = -5.0;


                //Double provjesN = ((poleSpan1-b1)*b1*reducedWeight)/(2*stvNap40);


                        //EditText b = (EditText) findViewById(R.id.b);


        TextView t1 = (TextView) findViewById(R.id.textView);
        TextView t2 = (TextView) findViewById(R.id.textView1);
        TextView t3 = (TextView) findViewById(R.id.textView2);
        TextView t4 = (TextView) findViewById(R.id.textView3);
        TextView t5 = (TextView) findViewById(R.id.textView4);
        TextView t6 = (TextView) findViewById(R.id.textView5);
        TextView t7 = (TextView) findViewById(R.id.textView6);


        Double surface = Double.parseDouble(surfaceInput.getText().toString());
        Double diameter = Double.parseDouble(diameterInput.getText().toString());
        Double mass = Double.parseDouble(massInput.getText().toString());
        Double malleabilityFactor = Double.parseDouble(malleabilityFactorInput.getText().toString());
        Double excessWeightFactor = Double.parseDouble(excessWeightFactorInput.getText().toString());
        Double elasticityModule = Double.parseDouble(elasticityModuleInput.getText().toString());
        Double normalStrain = Double.parseDouble(normalStrainInput.getText().toString());
        Double excessStrain = Double.parseDouble(excessStrainInput.getText().toString());
        Double maxStrain = Double.parseDouble(maxStrainInput.getText().toString());
        Double poleSpan = Double.parseDouble(poleSpanInput.getText().toString());
        Double poleDenivelation = Double.parseDouble(poleDenivelationInput.getText().toString());
        Double b1 = Double.parseDouble(b.getText().toString());






            Double temperature = -5.0;

            Double secCo1= - (strain - (((Math.pow(idealSpan, 2) / 24) * (Math.pow(reducedConductorWeight, 2) / Math.pow(strain, 2)) - (malleabilityFactor1 * (temperature - (-20)))) * elasticityModule1));
            Double secCo2= - (strain - (((Math.pow(idealSpan, 2) / 24) * (Math.pow(reducedConductorWeight, 2) / Math.pow(strain, 2)) - (malleabilityFactor1 * (temperature - (-5)))) * elasticityModule1));
            Double secCo3= - (strain - (((Math.pow(idealSpan, 2) / 24) * (Math.pow(reducedConductorWeight, 2) / Math.pow(strain, 2)) - (malleabilityFactor1 * (temperature - 40))) * elasticityModule1));

            Double secondCo1 = (strain / elasticityModule1) + (malleabilityFactor1 * (-5 - temperature)) - ((Math.pow(idealSpan, 2) / 24) * (Math.pow(reducedWeight, 2) / Math.pow(strain, 2)));
            //Double secondCo2 = (strain/elasticityModule1) + (malleabilityFactor1 * (temperature - (-5))) - ((Math.pow(idealSpan,2)/24)* (Math.pow(reducedWeight,2) / Math.pow(strain,2)));
            //Double secondCo3 = (strain/elasticityModule1) + (malleabilityFactor1 * (temperature - (-5))) - ((Math.pow(idealSpan,2)/24)* (Math.pow(reducedWeight,2) / Math.pow(strain,2)));
            Double thirdCo1 = -((Math.pow(idealSpan, 2) * Math.pow(reducedWeight, 2)) / 24) * elasticityModule1;
            Double thirdCo2 = -((Math.pow(idealSpan, 2) * Math.pow(reducedConductorWeight, 2)) / 24) * elasticityModule1;
            Double thirdCo3 = -((Math.pow(idealSpan, 2) * Math.pow(reducedWeight, 2)) / 24) * elasticityModule1;
            //Double thirdCo2 = (Math.pow(idealSpan, 2) * Math.pow(reducedWeight, 2)) / 24;

            Cubic cub = new Cubic();

            cub.solve(1.0, secCo1, 0.0, thirdCo1);

            Double strainRaw1 = cub.x1;

            Cubic cub2 = new Cubic();

            cub2.solve(1.0, secCo2, 0.0, thirdCo2);

            Double strainRaw2 = cub2.x1;

            Cubic cub3 = new Cubic();

            cub3.solve(1.0, secCo3, 0.0, thirdCo3);

            Double strainRaw3 = cub3.x1;




            Double stvNap20 = strainRaw1 * ((Math.pow(directDistance,2)/idealSpan)/((Math.pow(directDistance,3)/Math.pow(idealSpan,2))));

            Double provjes = ((Math.pow(idealSpan,2)*reducedWeight)/(8*stvNap20))*(directDistance/idealSpan);



            Double stvNap5 = strainRaw2 * ((Math.pow(directDistance,2)/idealSpan)/((Math.pow(directDistance,3)/Math.pow(idealSpan,2))));

            Double provjes1 = ((Math.pow(idealSpan,2)*reducedConductorWeight)/(8*stvNap5))*(directDistance/idealSpan);


            Double stvNap40 = strainRaw3 * ((Math.pow(directDistance,2)/idealSpan)/((Math.pow(directDistance,3)/Math.pow(idealSpan,2))));

            Double provjes2 = ((Math.pow(idealSpan,2)*reducedWeight)/(8*stvNap40))*(directDistance/idealSpan);


            Double provjesN = ((poleSpan1-b1)*b1*reducedWeight)/(2*stvNap40);


            t1.setText(Double.toString(provjes));
            t2.setText(Double.toString(provjes1));
            t3.setText(Double.toString(provjes2));

            t4.setText(Double.toString(secCo3));
            t5.setText(Double.toString(thirdCo3));
            t6.setText(Double.toString(idealSpan));
            t7.setText(Double.toString(provjesN));





    }
}
*/