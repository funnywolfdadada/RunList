# RunList  
一个简单的线程切换类，链式调用，实现简单的线程切换与参数传递。  
例：  
`  
        RunList.runOnBackground(new RunList.IRun<Integer, String>() {  
                    @Override  
                    public String run(Integer integer) {  
                        Log.d(TAG, "run: " + Thread.currentThread().getName()  
                                + ", " + integer);  
                        return String.valueOf(integer + 1);  
                    }  
                }, 1)  
                .runOnUiThread(new RunList.IRun<String, Integer>() {  
                    @Override
                    public Integer run(String string) {
                        Log.d(TAG, "run: " + Thread.currentThread().getName()
                                + ", " + string);
                        return Integer.valueOf(string + 1);
                    }
                })
                .runOnBackground(new RunList.IRun<Integer, String>() {
                    @Override
                    public String run(Integer integer) {
                        Log.d(TAG, "run: " + Thread.currentThread().getName()
                                + ", " + integer);
                        return String.valueOf(integer + 1);
                    }
                })
                .runOnUiThread(new RunList.IRun<String, Integer>() {
                    @Override
                    public Integer run(String string) {
                        Log.d(TAG, "run: " + Thread.currentThread().getName()
                                + ", " + string);
                        return Integer.valueOf(string + 1);
                    }
                })
                .start();
`
