package cn.salesuite.saf.rxjava.eventbus;

import com.safframwork.tony.common.utils.Preconditions;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.Subject;

/**
 * Created by Tony Shen on 15/10/26.
 */
public class RxEventBus {

    private static final String TAG = RxEventBus.class.getSimpleName();
    private static RxEventBus instance;

    public static synchronized RxEventBus getInstance() {
        if (instance == null) {
            instance = new RxEventBus();
        }
        return instance;
    }

    private RxEventBus() {
    }

    private ConcurrentHashMap<String, List<Subject>> subjectMapper = new ConcurrentHashMap<>();

    public <T> Observable<T> register(Class<T> clazz) {
        String tag = clazz.getName();
        List<Subject> subjectList = subjectMapper.get(tag);
        if (Preconditions.isBlank(subjectList)) {
            subjectList = new ArrayList<>();
            subjectMapper.put(tag, subjectList);
        }

        Subject<T, T> subject = PublishSubject.create();
        subjectList.add(subject);

        return subject;
    }

    public void unregister(String tag, Observable observable) {
        List<Subject> subjects = subjectMapper.get(tag);
        if (Preconditions.isNotBlank(subjects)) {
            subjects.remove((Subject) observable);
            if (Preconditions.isBlank(subjects)) {
                subjectMapper.remove(tag);
            }
        }
    }

    public void post(Object content) {
        String key = content.getClass().getName();
        List<Subject> subjectList = subjectMapper.get(key);

        if (Preconditions.isNotBlank(subjectList)) {
            for (Subject subject : subjectList) {
                subject.onNext(content);
            }
        }
    }
}
