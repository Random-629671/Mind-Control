package vjp.pro.stressverifier.data;

import java.util.ArrayList;
import java.util.List;

import vjp.pro.stressverifier.model.Question;
import vjp.pro.stressverifier.model.Solution;
import vjp.pro.stressverifier.enums.SolutionType;
import vjp.pro.stressverifier.enums.StressLevel;

public class MockDataStore {

    // Bộ câu hỏi PSS-10 rút gọn
    public static List<Question> getQuestions() {
        List<Question> list = new ArrayList<>();
        list.add(new Question(1, "Bạn có cảm thấy khó chịu vì những điều xảy ra bất ngờ?", false));
        list.add(new Question(2, "Bạn có cảm thấy không thể kiểm soát những việc quan trọng?", false));
        list.add(new Question(3, "Bạn có hay cảm thấy căng thẳng/stress?", false));
        list.add(new Question(4, "Bạn có tự tin vào khả năng xử lý vấn đề của mình?", false)); // Câu hỏi đảo ngược (nếu muốn logic xịn)
        // ... thêm câu hỏi

        // Câu hỏi tự luận (Chỉ hiện khi bật Mode AI)
        list.add(new Question(99, "Chia sẻ cụ thể vấn đề bạn đang gặp phải?", true));
        return list;
    }

    // Kho giải pháp (Local Database giả lập)
    public static List<Solution> getSolutions(StressLevel level) {
        List<Solution> list = new ArrayList<>();

        switch (level) {
            case HIGH:
                list.add(new Solution("Thiền buông thư", "Nội dung...", SolutionType.EXERCISE));
                list.add(new Solution("Tiếng mưa rơi", "raw/rain.mp3", SolutionType.MUSIC));
                break;
            case MEDIUM:
                list.add(new Solution("Kỹ thuật Pomodoro", "Nội dung...", SolutionType.READING));
                list.add(new Solution("Nhạc Lofi Chill", "raw/lofi.mp3", SolutionType.MUSIC));
                break;
            case LOW:
            default:
                list.add(new Solution("Đọc sách tích cực", "Nội dung...", SolutionType.READING));
                break;
        }
        return list;
    }
}