package vjp.pro.stressverifier.data;

import java.util.ArrayList;
import java.util.List;

import vjp.pro.stressverifier.model.Question;
import vjp.pro.stressverifier.model.Solution;
import vjp.pro.stressverifier.enums.SolutionType;
import vjp.pro.stressverifier.enums.StressLevel;
import vjp.pro.stressverifier.model.SurveyStage;

public class MockDataStore {
    private static List<Question.Option> createOptions(String... textsAndScores) {
        List<Question.Option> options = new ArrayList<>();
        for (String s : textsAndScores) {
            String[] parts = s.split(":");
            options.add(new Question.Option(parts[0], Integer.parseInt(parts[1])));
        }
        return options;
    }

    private static final List<Question.Option> STD_OPTIONS = createOptions(
            "Không bao giờ:0", "Hiếm khi:1", "Thỉnh thoảng:2", "Thường xuyên:3", "Rất thường xuyên:4"
    );
    public static List<SurveyStage> getAllStages() {
        // Todo / Optimal: Add more question so it can use random question in each phase / random phase.
        List<SurveyStage> stages = new ArrayList<>();

        List<Question> q1 = new ArrayList<>();
        q1.add(new Question(101, "Bạn thường xuyên bị đau đầu hoặc đau cơ không rõ nguyên nhân?", false, STD_OPTIONS));
        q1.add(new Question(102, "Bạn có gặp vấn đề về tiêu hóa (đau dạ dày, khó tiêu) gần đây?", false, STD_OPTIONS));
        q1.add(new Question(103, "Tim bạn có hay đập nhanh hoặc cảm thấy tức ngực?", false, STD_OPTIONS));
        q1.add(new Question(104, "Giấc ngủ của bạn có bị gián đoạn (khó ngủ, hay tỉnh giấc) không?", false, STD_OPTIONS));
        stages.add(new SurveyStage(1, "Giai đoạn 1: Biểu hiện Vật lý", "Đánh giá các phản ứng của cơ thể bạn.", q1, 4));

        List<Question> q2 = new ArrayList<>();
        q2.add(new Question(201, "Bạn có dễ dàng cảm thấy bực bội hoặc nóng giận vô cớ?", false, STD_OPTIONS));
        q2.add(new Question(202, "Bạn có cảm thấy lo lắng, bồn chồn thường xuyên không?", false, STD_OPTIONS));
        q2.add(new Question(203, "Bạn có cảm thấy buồn bã hoặc trống rỗng kéo dài?", false, STD_OPTIONS));
        q2.add(new Question(204, "Bạn có cảm thấy mất hứng thú với những sở thích cũ?", false, STD_OPTIONS));
        stages.add(new SurveyStage(2, "Giai đoạn 2: Cảm xúc & Tâm trạng", "Kiểm tra trạng thái tinh thần của bạn.", q2, 4));

        List<Question> q3 = new ArrayList<>();
        q3.add(new Question(301, "Bạn có xu hướng trì hoãn công việc quan trọng?", false, STD_OPTIONS));
        q3.add(new Question(302, "Bạn có thay đổi thói quen ăn uống (ăn quá nhiều hoặc quá ít)?", false, STD_OPTIONS));
        q3.add(new Question(303, "Bạn có đang lạm dụng chất kích thích (rượu, bia, caffeine)?", false, STD_OPTIONS));
        q3.add(new Question(304, "Bạn có thói quen cắn móng tay, rung đùi hoặc đi lại liên tục?", false, STD_OPTIONS));
        stages.add(new SurveyStage(3, "Giai đoạn 3: Thay đổi Hành vi", "Quan sát các thói quen hàng ngày.", q3, 4));

        List<Question> q4 = new ArrayList<>();
        q4.add(new Question(401, "Bạn có gặp khó khăn trong việc tập trung vào một việc?", false, STD_OPTIONS));
        q4.add(new Question(402, "Bạn có hay quên những việc lặt vặt hàng ngày?", false, STD_OPTIONS));
        q4.add(new Question(403, "Bạn có thường xuyên suy nghĩ tiêu cực về bản thân?", false, STD_OPTIONS));
        q4.add(new Question(404, "Bạn có thấy khó khăn khi phải đưa ra quyết định?", false, STD_OPTIONS));
        stages.add(new SurveyStage(4, "Giai đoạn 4: Chức năng Nhận thức", "Đánh giá khả năng suy nghĩ và tập trung.", q4, 4));

        List<Question> q5 = new ArrayList<>();
        q5.add(new Question(501, "Bạn có đang thu mình, né tránh gặp gỡ bạn bè/người thân?", false, STD_OPTIONS));
        q5.add(new Question(502, "Bạn có dễ nảy sinh xung đột với đồng nghiệp hoặc gia đình?", false, STD_OPTIONS));
        q5.add(new Question(503, "Bạn có cảm thấy không ai hiểu được vấn đề của mình?", false, STD_OPTIONS));
        q5.add(new Question(504, "Bạn có cảm thấy cô đơn ngay cả khi ở giữa đám đông?", false, STD_OPTIONS));
        stages.add(new SurveyStage(5, "Giai đoạn 5: Tương tác Xã hội", "Mối quan hệ của bạn với môi trường xung quanh.", q5, 4));

        List<Question> q6 = new ArrayList<>();
        q6.add(new Question(601, "Bạn có cảm thấy quá tải với khối lượng công việc hiện tại?", false, STD_OPTIONS));
        q6.add(new Question(602, "Bạn có cảm thấy công việc của mình vô nghĩa?", false, STD_OPTIONS));
        q6.add(new Question(603, "Bạn có thường xuyên mang việc về nhà làm không?", false, STD_OPTIONS));
        q6.add(new Question(604, "Bạn có lo sợ bị sa thải hoặc thất bại trong công việc?", false, STD_OPTIONS));
        stages.add(new SurveyStage(6, "Giai đoạn 6: Áp lực Công việc", "Đánh giá stress từ môi trường làm việc.", q6, 4));

        List<Question> q7 = new ArrayList<>();
        q7.add(new Question(701, "Hãy mô tả ngắn gọn điều gì làm bạn lo lắng nhất lúc này (Text)?", true, null));
        q7.add(new Question(702, "Bạn mong muốn thay đổi điều gì nhất trong cuộc sống hiện tại? (Text)", true, null));
        stages.add(new SurveyStage(7, "Giai đoạn 7: Phân tích sâu", "Chia sẻ thêm để AI hỗ trợ tốt hơn.", q7, 2));

        return stages;
    }

    public static List<Solution> getSolutions(StressLevel level) {
        List<Solution> list = new ArrayList<>();

        switch (level) {
            // Todo / Optimal: Verify solution link and add more solution.
            case HIGH:
                list.add(new Solution("Nhạc sóng não Delta (Ngủ ngon)", "https://www.youtube.com/watch?v=M5z2bVfXyps", SolutionType.MUSIC));
                list.add(new Solution("Bài tập thở 4-7-8 (Giảm lo âu)", "https://www.youtube.com/watch?v=LiUnFJ8P4tM", SolutionType.EXERCISE));
                break;
            case MEDIUM:
                list.add(new Solution("Nhạc Lofi Chill (Tập trung)", "https://www.youtube.com/watch?v=jfKfPfyJRdk", SolutionType.MUSIC));
                list.add(new Solution("Phương pháp Pomodoro", "https://todoist.com/productivity-methods/pomodoro-technique", SolutionType.READING));
                break;
            case LOW:
            default:
                list.add(new Solution("Podcast: Sống tích cực", "https://www.youtube.com/watch?v=some_podcast_link", SolutionType.MUSIC));
                list.add(new Solution("Sách: Đắc Nhân Tâm (Review)", "https://www.goodreads.com/", SolutionType.READING));
                break;
        }
        return list;
    }
}