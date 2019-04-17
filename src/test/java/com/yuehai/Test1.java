//package com.yuehai;
//
//
//import io.reactivex.Observable;
//import io.reactivex.ObservableEmitter;
//import io.reactivex.Observer;
//import io.reactivex.disposables.Disposable;
//import io.reactivex.schedulers.Schedulers;
//import org.junit.Test;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
///**
// * Created by zhaoyuehai 2019/3/28
// * 需要先添加依赖implementation 'io.reactivex.rxjava2:rxjava:2.2.2'
// */
//public class Test1 {
//    private static Logger logger = LoggerFactory.getLogger(Test1.class);
//
//    @Test
//    public void testRxJava2_1() {
//
//        //1.创建被观察者Observable
//        Observable<Object> observable = Observable.create(emitter -> {
//            emitter.onNext("000");
//            emitter.onNext("111");
//            emitter.onNext("222");
//            emitter.onNext("333");
//            emitter.onComplete();
//        });
//        //2.创建观察者Observer
//        Observer<Object> observer = new Observer<Object>() {
//            private Disposable mDisposable;
//
//            @Override
//            public void onSubscribe(Disposable d) {
//                logger.debug("onSubscribe");
//                mDisposable = d;
//            }
//
//            @Override
//            public void onNext(Object o) {
//                logger.debug("onNext: {} ", o);
//                if (o.equals("222")) {
//                    mDisposable.dispose();
//                }
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                logger.debug("onError");
//            }
//
//            @Override
//            public void onComplete() {
//                logger.debug("onComplete");
//            }
//        };
//        //3.被观察者去订阅观察者
//        //【注意：在普通的观察者模式中，是观察者订阅被观察者。但是在RxJava2中是被观察者订阅了观察者，这样做是为了方便开发者链式调用。】
//        observable.subscribe(observer);
//    }
//
//    /**
//     * Observable.create是个静态方法
//     * <p>
//     * 用户实现的ObservableOnSubscribe回调方法subscribe(ObservableEmitter<T> emitter)，emitter用来发送事件
//     * <p>
//     * 他用用户的ObservableOnSubscribe回调对象source作为参数
//     * <p>
//     * 创建一个 ObservableCreate(source)类
//     * 该ObservableCreate类继承自Observable并实现了抽象方法subscribeActual(Observer observer)
//     * <p>
//     * 该subscribeActual方法其实就是订阅被观察者observer，即在Observable的subscribe(Observer observer)方法调用时执行
//     * 该subscribeActual方法中创建了CreateEmitter(observer)类
//     * <p>
//     * CreateEmitter就是用户ObservableOnSubscribe回调方法subscribe(ObservableEmitter<T> emitter)的emitter，用来发送事件
//     * <p>
//     * <p>
//     * 最终流程：
//     * Observable的subscribe(Observer observer)方法调用
//     * <p>
//     * -->ObservableCreate的subscribeActual(Observer observer)方法调用
//     * <p>
//     * -->创建CreateEmitter(Observer observer)类
//     * -->Observer在此时执行onSubscribe(Disposable d)事件;
//     * -->用户的ObservableOnSubscribe回调方法subscribe(ObservableEmitter<T> emitter)执行
//     * <p>
//     * -->即CreateEmitter执行了onNext(Object o)/onComplete()/onError(Throwable e)等事件
//     * -->在CreateEmitter构造方法已经关联的Observer执行了对应的onNext(Object o)/onComplete()/onError(Throwable e)等事件
//     */
//
//    private final ObservableEmitter observableEmitter = null;
//
//    @Test
//    public void testRxJava2_2() {
//        Observable<String> observeOn = Observable.create(emitter -> {
////            observableEmitter = emitter;
//            logger.debug("----emitter:onNext");
//            emitter.onNext("Hello RxJava2!");
//            emitter.onComplete();
//        });//observeOns1 --> ObservableSubscribeOn
////        Observable<String> observeOns1 = observeOn.subscribeOn(Schedulers.computation());//observeOns2 --> ObservableSubscribeOn
//        Observable<String> observeOns2 = observeOn.subscribeOn(Schedulers.io());
//        //observeOn1 --> ObservableObserverOn
//        Observable<String> observeOn1 = observeOns2.observeOn(Schedulers.io());
//        //observeOn2 --> ObservableObserverOnEach
////        Observable<String> observeOn2 = observeOn1.doOnNext(s -> logger.debug("----" + "doOnNext--> accept:" + s));
//        //observeOn3 --> ObservableObserverOn
////        Observable<String> observeOn3 = observeOn2.observeOn(Schedulers.newThread());
//        observeOn1.subscribe(new Observer<String>() {
//            @Override
//            public void onSubscribe(Disposable d) {
//                logger.debug("----onSubscribe");
//            }
//
//            @Override
//            public void onNext(String s) {
//                logger.debug("----onNext:" + s);
//            }
//
//            @Override
//            public void onError(Throwable e) {
//
//            }
//
//            @Override
//            public void onComplete() {
//                logger.debug("----onComplete");
//            }
//        });
//    }
//
//    @Test
//    public void testRxJava2_3() {
//        Observable.just(10086)
//                .map(integer -> "转换成字符串后:" + integer)
//                .subscribe(new Observer<String>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//                        logger.debug("onSubscribe");
//                    }
//
//                    @Override
//                    public void onNext(String s) {
//                        logger.debug("onNext: {} ", s);
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        logger.debug("onError");
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        logger.debug("onComplete");
//                    }
//                });
//    }
//}
