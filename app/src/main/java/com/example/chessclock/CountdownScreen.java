package com.example.chessclock;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CountdownScreen extends Screen {

    private boolean showTot;
    private boolean showMov;
    private boolean showingDialog;
    private CountDownTimer cdWhiteTot;
    private CountDownTimer cdWhiteMov;
    private CountDownTimer cdBlackTot;
    private CountDownTimer cdBlackMov;
    private int currentPlayerToMove;
    private int startGame;
    private int remainingWhiteTot;
    private int remainingBlackTot;

    int min(int a, int b) {
        if(a < b)
            return a;
        return b;
    }

    int min(int a, int b, int c) {
        if(a < b)
            return min(a, c);
        return min(b, c);
    }

    String intToTime(int x) {
        StringBuilder str = new StringBuilder();

        boolean created = false;

        if(x >= 3600) {
            int aux = x / 3600;
            str.append(Integer.toString(aux));
            x %= 3600;
            created = true;
            str.append(":");
        }

        if(x >= 60) {

            int aux = x / 60;
            if(aux < 10 && created)
                str.append("0");

            str.append(Integer.toString(aux));

            created = true;
            str.append(":");
            x %= 60;

        } else if(created) {
            str.append(("00:"));
        }

        if(x > 0) {
            int aux = x;

            if(aux < 10 && created)
                str.append("0");

            str.append(Integer.toString(aux));

        } else if(created) {
            str.append(("00"));
        }

        return str.toString();


    }

    void createDialogAndGoBack(String title, String message) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        showingDialog = true;
        alert.setTitle(title);
        alert.setMessage(message);
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(getApplicationContext(), ChooseType.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        alert.setNegativeButton("Give One More Chance", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                showingDialog = false;
                if(currentPlayerToMove == 0) {
                    cdWhiteMov.resume();
                    cdWhiteTot.resume();
                    resumeBlackFromWhite();
                } else if(currentPlayerToMove == 1) {
                    cdBlackMov.resume();
                    cdBlackTot.resume();
                    resumeWhiteFromBlack();
                }
            }
        });
        alert.create().show();
    }

    void pauseAll() {

        if(currentPlayerToMove == 0) {
            cdWhiteMov.pause();
            cdWhiteTot.pause();
        } else if(currentPlayerToMove == 1) {
            cdBlackMov.pause();
            cdBlackTot.pause();
        }

    }

    void cancelAll() {
        cdWhiteTot.cancel();
        cdWhiteMov.cancel();
        cdBlackTot.cancel();
        cdBlackMov.cancel();
    }

    void getFromLastActivity() {
        this.showMov = getBoolVarFromLastActivity("showMov");
        this.showTot = getBoolVarFromLastActivity("showTot");
    }

    void setCdWhiteTot() {

        int time = timeInTvTot()*1000;
        final TextView tvWhiteTot = (TextView) findViewById(R.id.tvWhiteTotal);
        cdWhiteTot = new CountDownTimer(time, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int x = (int)millisUntilFinished;
                remainingWhiteTot = (int)millisUntilFinished;
                x /= 1000;
                if(x <= 30) {
                    setColorTextView(tvWhiteTot, "#cc0000");
                }
                tvWhiteTot.setText(intToTime(x));
            }

            @Override
            public void onFinish() {
                tvWhiteTot.setText("0");
                if(showTot && showMov) {
                    pauseAll();
                    createDialogAndGoBack("Out Of Time", "Black player wins");
                }
            }
        };

    }

    void setCdWhiteMov() {

        int time = timeInTvMov(remainingWhiteTot);
        final TextView tvWhiteMov = (TextView) findViewById(R.id.tvWhiteMove);
        final TextView tvWhiteTot = (TextView) findViewById(R.id.tvWhiteTotal);

        cdWhiteMov = new CountDownTimer(time, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int x = (int)millisUntilFinished;
                x = min(x, remainingWhiteTot);
                x /= 1000;
                if(x > 5) {
                    setColorTextView(tvWhiteMov, "#00cc00");
                } else {
                    setColorTextView(tvWhiteMov, "#cc0000");
                }
                tvWhiteMov.setText(intToTime(x));
            }

            @Override
            public void onFinish() {
                tvWhiteMov.setText("0");
                pauseAll();
                createDialogAndGoBack("Out Of Time", "Black player wins");
            }
        };
    }

    void setCdBlackTot() {
        int time = timeInTvTot()*1000;
        final TextView tvBlackTot = (TextView) findViewById(R.id.tvBlackTotal);

        cdBlackTot = new CountDownTimer(time, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int x = (int) millisUntilFinished;
                remainingBlackTot = (int)millisUntilFinished;
                x /= 1000;
                if(x <= 30) {
                    setColorTextView(tvBlackTot, "#cc0000");
                }
                tvBlackTot.setText(intToTime(x));
            }

            @Override
            public void onFinish() {
                tvBlackTot.setText("0");
                if(showMov && showTot) {
                    pauseAll();
                    createDialogAndGoBack("Out Of Time", "White player wins");
                }
            }
        };
    }

    void setCdBlackMov() {

        int time = timeInTvMov(remainingBlackTot);
        final TextView tvBlackMov = (TextView) findViewById(R.id.tvBlackMove);

        cdBlackMov = new CountDownTimer(time, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int x = (int) millisUntilFinished;
                x = min(x, remainingBlackTot);
                x /= 1000;
                if(x > 5) {
                    setColorTextView(tvBlackMov, "#00cc00");
                } else {
                    setColorTextView(tvBlackMov, "#cc0000");
                }
                tvBlackMov.setText(intToTime(x));
            }

            @Override
            public void onFinish() {
                tvBlackMov.setText("0");
                pauseAll();
                createDialogAndGoBack("Out Of Time", "White player wins");
            }
        };
    }

    void setColorTextView(TextView tv, String color) {
        tv.setTextColor(Color.parseColor(color));
    }

    void buttonPauseActions() {
        final Button butPause = (Button) findViewById(R.id.butPause);
        butPause.setText("Start");
        butPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = butPause.getText().toString();
                if(text.equals("Start")) {
                    butPause.setText("Pause");
                    cdWhiteTot.start();
                    cdWhiteMov.start();
                    currentPlayerToMove = 0;
                    startGame++;
                } else if(text.equals("Pause")) {
                    butPause.setText("Resume");
                    if(currentPlayerToMove == 0) {
                        cdWhiteMov.pause();
                        cdWhiteTot.pause();
                    } else if(currentPlayerToMove == 1) {
                        cdBlackMov.pause();
                        cdBlackTot.pause();
                    }
                } else if(text.equals("Resume")) {
                    butPause.setText("Pause");
                    if(currentPlayerToMove == 0) {
                        cdWhiteTot.resume();
                        cdWhiteMov.resume();
                    } else if(currentPlayerToMove == 1) {
                        cdBlackTot.resume();
                        cdBlackMov.resume();
                    }
                }
            }
        });
    }

    void initVariables() {
        this.startGame = 0;
        // -1 = nobody, 0 = white, 1 = black
        this.currentPlayerToMove = -1;
        this.remainingBlackTot = 0x3FFFFFFF;
        this.remainingWhiteTot= 0x3FFFFFFF;
    }

    void resumeWhiteFromBlack() {

        cdBlackTot.pause();
        cdBlackMov.pause();
        setColorTextView(((TextView)findViewById(R.id.tvBlackMove)), "#FFFFFF");
        if(showMov) {
            resetTvBlackMov();
        }
        currentPlayerToMove = 0;

        if(showMov && showTot) {
            setCdBlackMov();
            cdWhiteTot.resume();
            cdWhiteMov.start();
        } else if(showMov) {
            setCdBlackMov();
            cdWhiteMov.start();
        } else if(showTot){
            cdWhiteMov.resume();
        }
    }

    void resumeBlackFromWhite() {
        cdWhiteTot.pause();
        cdWhiteMov.pause();
        setColorTextView(((TextView)findViewById(R.id.tvWhiteMove)), "#000000");
        if(showMov) {
            resetTvWhiteMov();
        }
        currentPlayerToMove = 1;

        if(startGame == 1) {
            if(showMov) {
                setCdWhiteMov();
            }
            cdBlackMov.start();
            cdBlackTot.start();
            startGame++;
        } else {
            if(showMov && showTot) {
                setCdWhiteMov();
                cdBlackTot.resume();
                cdBlackMov.start();
            } else if(showMov) {
                setCdWhiteMov();
                cdBlackMov.start();
            } else if(showTot) {
                cdBlackMov.resume();
            }
        }
    }

    void blackPlayerClick() {
        TextView tvBlackMove = (TextView) findViewById(R.id.tvBlackMove);
        TextView tvBlackTotal = (TextView) findViewById(R.id.tvBlackTotal);
        ConstraintLayout clBlack = (ConstraintLayout) findViewById(R.id.clBlack);

        tvBlackMove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(currentPlayerToMove != 1)
                    return;

                resumeWhiteFromBlack();

            }
        });

        tvBlackTotal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentPlayerToMove != 1)
                    return;

                resumeWhiteFromBlack();
            }
        });

        clBlack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentPlayerToMove != 1)
                    return;

                resumeWhiteFromBlack();
            }
        });
    }

    void whitePlayerClick() {
        TextView tvWhiteMove = (TextView) findViewById(R.id.tvWhiteMove);
        TextView tvWhiteTotal = (TextView) findViewById(R.id.tvWhiteTotal);
        ConstraintLayout clWhite = (ConstraintLayout) findViewById(R.id.clWhite);

        tvWhiteMove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentPlayerToMove != 0)
                    return;

                resumeBlackFromWhite();
            }
        });

        tvWhiteTotal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentPlayerToMove != 0)
                    return;

                resumeBlackFromWhite();
            }
        });

        clWhite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentPlayerToMove != 0)
                    return;

                resumeBlackFromWhite();
            }
        });

    }

    int timeInTvTot() {
        int timeTotal = (getIntVarFromLastActivity("timeTotal"))*60;
        return timeTotal;
    }

    int timeInTvMov(int remaining) {
        int timeTotal = (getIntVarFromLastActivity("timeTotal"))*60*1000;
        int timeMov = (getIntVarFromLastActivity("timeMov"))*1000;
        if(showMov && showTot) {
            return min(timeTotal, timeMov, remaining);
        } else if(showMov) {
            return min(timeMov, remaining);
        } else if(showTot) {
            return timeTotal;
        }

        return -1;
    }

    void hideElements() {

        if(showTot == false || showMov == false) {
            ((TextView) findViewById(R.id.tvBlackTotal)).setTextColor(Color.parseColor("#000000"));
            ((TextView) findViewById(R.id.tvWhiteTotal)).setTextColor(Color.parseColor("#ffffff"));
        }
    }

    void resetTvBlackMov() {
        if(showMov) {
            int timeTvMov = timeInTvMov(remainingBlackTot) / 1000;
            ((TextView)findViewById(R.id.tvBlackMove)).setText(intToTime(timeTvMov));
        }
    }

    void resetTvWhiteMov() {
        if(showMov) {
            int timeTvMov = timeInTvMov(remainingWhiteTot) / 1000;
            ((TextView)findViewById(R.id.tvWhiteMove)).setText(intToTime(timeTvMov));
        }
    }

    void initElements() {
        int timeTvTot = timeInTvTot();
        ((TextView)findViewById(R.id.tvWhiteTotal)).setText(intToTime(timeTvTot));
        ((TextView)findViewById(R.id.tvBlackTotal)).setText(intToTime(timeTvTot));

        int timeTvMov = timeInTvMov(remainingWhiteTot)  / 1000;
        ((TextView)findViewById(R.id.tvWhiteMove)).setText(intToTime(timeTvMov));
        timeTvMov = timeInTvMov(remainingBlackTot)  / 1000;
        ((TextView)findViewById(R.id.tvBlackMove)).setText(intToTime(timeTvMov));
        showingDialog = false;
    }

    void butEndClick() {

        ((Button) findViewById(R.id.butEndGame)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(currentPlayerToMove == 0) {
                    cdWhiteMov.pause();
                    cdWhiteTot.pause();
                } else if(currentPlayerToMove == 1) {
                    cdBlackMov.pause();
                    cdBlackTot.pause();
                }

                AlertDialog.Builder alert = new AlertDialog.Builder(CountdownScreen.this);
                alert.setTitle("Alert");
                alert.setMessage("Do you want quit the game?");
                alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        cancelAll();
                        Intent intent = new Intent(getApplicationContext(), ChooseType.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }
                });
                alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(currentPlayerToMove == 0) {
                            cdWhiteTot.resume();
                            cdWhiteMov.resume();
                        } else if(currentPlayerToMove == 1) {
                            cdBlackTot.resume();
                            cdBlackMov.resume();
                        }
                    }
                });
                alert.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        if(currentPlayerToMove == 0) {
                            cdWhiteTot.resume();
                            cdWhiteMov.resume();
                        } else if(currentPlayerToMove == 1) {
                            cdBlackTot.resume();
                            cdBlackMov.resume();
                        }
                    }
                });
                alert.create().show();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(showingDialog) {
            showingDialog = false;
            if(currentPlayerToMove == 0) {
                cdWhiteMov.resume();
                cdWhiteTot.resume();
                resumeBlackFromWhite();
            } else if(currentPlayerToMove == 1) {
                cdBlackMov.resume();
                cdBlackTot.resume();
                resumeWhiteFromBlack();
            }
        } else {
            cancelAll();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_countdown_screen);

        getFromLastActivity();
        initVariables();
        initElements();
        hideElements();
        setCdWhiteTot();
        setCdWhiteMov();
        setCdBlackTot();
        setCdBlackMov();

        whitePlayerClick();
        blackPlayerClick();

        buttonPauseActions();
        butEndClick();

    }
}
