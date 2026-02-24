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

    private static final List<Question.Option> FREQ_3_OPTIONS = createOptions(
            "Gần như không:0", "Có một chút:2", "Bị rất nặng:4"
    );

    private static final List<Question.Option> YES_NO_OPTIONS = createOptions(
            "Không:0", "Có, tôi đang bị:3"
    );


    public static List<SurveyStage> getAllStages() {
        List<SurveyStage> stages = new ArrayList<>();

        List<Question> q1 = new ArrayList<>();
        q1.add(new Question(101, "Bạn thường xuyên bị đau đầu hoặc đau cơ không rõ nguyên nhân?", false, STD_OPTIONS));
        q1.add(new Question(102, "Bạn có gặp vấn đề về tiêu hóa (đau dạ dày, khó tiêu) gần đây?", false, STD_OPTIONS));
        q1.add(new Question(103, "Tim bạn có hay đập nhanh hoặc cảm thấy tức ngực?", false, STD_OPTIONS));
        q1.add(new Question(104, "Giấc ngủ của bạn có bị gián đoạn (khó ngủ, hay tỉnh giấc) không?", false, STD_OPTIONS));
        q1.add(new Question(105, "Gần đây bạn có bị rụng tóc nhiều bất thường không?", false, YES_NO_OPTIONS));
        q1.add(new Question(106, "Mức độ thèm ăn của bạn dạo này thế nào?", false, FREQ_3_OPTIONS));
        q1.add(new Question(107, "Bạn có cảm thấy mệt mỏi rã rời ngay cả khi vừa ngủ dậy?", false, STD_OPTIONS));
        q1.add(new Question(108, "Bạn có hay bị đổ mồ hôi tay hoặc lạnh tay chân không?", false, STD_OPTIONS));
        q1.add(new Question(109, "Bạn có gặp tình trạng nghiến răng khi ngủ hoặc cứng hàm không?", false, YES_NO_OPTIONS));
        q1.add(new Question(110, "Tần suất bạn bị cảm lạnh hoặc ốm vặt gần đây có tăng không?", false, FREQ_3_OPTIONS));
        stages.add(new SurveyStage(1, "Giai đoạn 1: Biểu hiện Vật lý", "Đánh giá các phản ứng của cơ thể bạn.", q1, 4));

        List<Question> q2 = new ArrayList<>();
        q2.add(new Question(201, "Bạn có dễ dàng cảm thấy bực bội hoặc nóng giận vô cớ?", false, STD_OPTIONS));
        q2.add(new Question(202, "Bạn có cảm thấy lo lắng, bồn chồn thường xuyên không?", false, STD_OPTIONS));
        q2.add(new Question(203, "Bạn có cảm thấy buồn bã hoặc trống rỗng kéo dài?", false, STD_OPTIONS));
        q2.add(new Question(204, "Bạn có cảm thấy mất hứng thú với những sở thích cũ?", false, STD_OPTIONS));
        q2.add(new Question(205, "Cảm giác muốn khóc ập đến đột ngột với bạn?", false, FREQ_3_OPTIONS));
        q2.add(new Question(206, "Bạn có thấy mình dễ bị tổn thương bởi lời nói của người khác hơn bình thường?", false, STD_OPTIONS));
        q2.add(new Question(207, "Bạn có cảm thấy tuyệt vọng về tương lai không?", false, STD_OPTIONS));
        q2.add(new Question(208, "Bạn có cảm thấy mình đang mất kiểm soát cuộc sống?", false, FREQ_3_OPTIONS));
        q2.add(new Question(209, "Bạn có thường xuyên cảm thấy tội lỗi dù không làm gì sai?", false, STD_OPTIONS));
        stages.add(new SurveyStage(2, "Giai đoạn 2: Cảm xúc & Tâm trạng", "Kiểm tra trạng thái tinh thần của bạn.", q2, 4));

        List<Question> q3 = new ArrayList<>();
        q3.add(new Question(301, "Bạn có xu hướng trì hoãn công việc quan trọng?", false, STD_OPTIONS));
        q3.add(new Question(302, "Bạn có thay đổi thói quen ăn uống (ăn quá nhiều hoặc quá ít)?", false, STD_OPTIONS));
        q3.add(new Question(303, "Bạn có đang lạm dụng chất kích thích (rượu, bia, thuốc lá, cafein)?", false, YES_NO_OPTIONS));
        q3.add(new Question(304, "Bạn có thói quen cắn móng tay, rung đùi hoặc đi lại liên tục?", false, STD_OPTIONS));
        q3.add(new Question(305, "Bạn có xu hướng lái xe nhanh hơn hoặc mạo hiểm hơn khi stress?", false, YES_NO_OPTIONS));
        q3.add(new Question(306, "Bạn có bỏ bê việc chăm sóc ngoại hình (vệ sinh cá nhân, quần áo)?", false, STD_OPTIONS));
        q3.add(new Question(307, "Bạn có kiểm tra điện thoại/tin nhắn một cách cưỡng chế không?", false, STD_OPTIONS));
        q3.add(new Question(308, "Bạn có đang tránh né các cuộc gọi hoặc tin nhắn từ người thân?", false, STD_OPTIONS));
        stages.add(new SurveyStage(3, "Giai đoạn 3: Thay đổi Hành vi", "Quan sát các thói quen hàng ngày.", q3, 4));

        List<Question> q4 = new ArrayList<>();
        q4.add(new Question(401, "Bạn có gặp khó khăn trong việc tập trung vào một việc?", false, STD_OPTIONS));
        q4.add(new Question(402, "Bạn có hay quên những việc lặt vặt hàng ngày?", false, FREQ_3_OPTIONS));
        q4.add(new Question(403, "Bạn có thường xuyên suy nghĩ tiêu cực về bản thân?", false, STD_OPTIONS));
        q4.add(new Question(404, "Bạn có thấy khó khăn khi phải đưa ra các quyết định đơn giản?", false, YES_NO_OPTIONS));
        q4.add(new Question(405, "Đầu óc bạn có thường xuyên 'trống rỗng' khi cần suy nghĩ?", false, STD_OPTIONS));
        q4.add(new Question(406, "Bạn có thường xuyên lo lắng quá mức về những chuyện chưa xảy ra?", false, STD_OPTIONS));
        q4.add(new Question(407, "Khả năng sáng tạo của bạn dạo này có bị sụt giảm?", false, FREQ_3_OPTIONS));
        q4.add(new Question(408, "Bạn có cảm thấy ác mộng thường xuyên xuất hiện không?", false, YES_NO_OPTIONS));
        stages.add(new SurveyStage(4, "Giai đoạn 4: Chức năng Nhận thức", "Đánh giá khả năng suy nghĩ và tập trung.", q4, 4));

        List<Question> q5 = new ArrayList<>();
        q5.add(new Question(501, "Bạn có đang thu mình, né tránh gặp gỡ bạn bè/người thân?", false, STD_OPTIONS));
        q5.add(new Question(502, "Bạn có dễ nảy sinh xung đột với đồng nghiệp hoặc gia đình?", false, STD_OPTIONS));
        q5.add(new Question(503, "Bạn có cảm thấy không ai hiểu được vấn đề của mình?", false, STD_OPTIONS));
        q5.add(new Question(504, "Bạn có cảm thấy cô đơn ngay cả khi đang ở cùng người khác?", false, STD_OPTIONS));
        q5.add(new Question(505, "Bạn có mất kiên nhẫn khi phải lắng nghe người khác nói?", false, FREQ_3_OPTIONS));
        q5.add(new Question(506, "Bạn có cảm thấy e ngại khi phải bắt đầu một cuộc trò chuyện mới?", false, STD_OPTIONS));
        q5.add(new Question(507, "Bạn có xu hướng đổ lỗi cho người khác về các vấn đề của mình?", false, YES_NO_OPTIONS));
        stages.add(new SurveyStage(5, "Giai đoạn 5: Tương tác Xã hội", "Mối quan hệ của bạn với môi trường xung quanh.", q5, 4));

        List<Question> q6 = new ArrayList<>();
        q6.add(new Question(601, "Bạn có cảm thấy quá tải với khối lượng công việc/bài tập hiện tại?", false, STD_OPTIONS));
        q6.add(new Question(602, "Bạn có cảm thấy nỗ lực của mình không được công nhận?", false, FREQ_3_OPTIONS));
        q6.add(new Question(603, "Bạn có thường xuyên phải làm việc ngoài giờ (Overtime/Thức đêm)?", false, YES_NO_OPTIONS));
        q6.add(new Question(604, "Môi trường sống/làm việc của bạn có quá ồn ào hay ngột ngạt?", false, STD_OPTIONS));
        q6.add(new Question(605, "Bạn có đang gặp áp lực về vấn đề tài chính cá nhân?", false, STD_OPTIONS));
        q6.add(new Question(606, "Thời gian di chuyển (đi học/đi làm) có làm bạn kiệt sức?", false, STD_OPTIONS));
        q6.add(new Question(607, "Bạn có cảm thấy thiếu không gian riêng tư cho bản thân?", false, FREQ_3_OPTIONS));
        stages.add(new SurveyStage(6, "Giai đoạn 6: Áp lực Môi trường", "Đánh giá stress từ môi trường làm việc/học tập.", q6, 4));

        List<Question> q7 = new ArrayList<>();
        q7.add(new Question(701, "Hãy chia sẻ ngắn gọn điều gì làm bạn áp lực nhất lúc này?", true, null));
        q7.add(new Question(702, "Bạn mong muốn thay đổi điều gì nhất trong cuộc sống hiện tại?", true, null));
        q7.add(new Question(703, "Kể tên một sự kiện gần đây khiến bạn cảm thấy không thoải mái nhất?", true, null));
        q7.add(new Question(704, "Nếu có một ngày hoàn toàn tự do, bạn sẽ làm gì để thư giãn?", true, null));
        stages.add(new SurveyStage(7, "Giai đoạn 7: Phân tích sâu", "Chia sẻ thêm để AI hỗ trợ tốt hơn.", q7, 3));

        return stages;
    }

    public static List<Solution> getSolutions(StressLevel level) {
        List<Solution> list = new ArrayList<>();

        switch (level) {
            case EXTREME:
            case HIGH:
                list.add(new Solution("Bài tập thở 4-7-8 (Giảm hoảng loạn ngay lập tức)", "https://www.youtube.com/watch?v=LiUnFJ8P4tM", SolutionType.EXERCISE));
                list.add(new Solution("Nhạc Sóng Delta (Trị liệu mất ngủ sâu)", "https://www.youtube.com/watch?v=xQ6xgDI7Whc", SolutionType.MUSIC));
                list.add(new Solution("Tham vấn tâm lý (Kết nối đường dây nóng)", "https://daytre.com/tu-van-tam-ly/", SolutionType.READING));
                list.add(new Solution("Video: Cách vượt qua hội chứng Burnout (Kiệt sức)", "https://www.youtube.com/watch?v=k-MW_4eD_E8", SolutionType.READING));
                break;

            case MEDIUM:
                list.add(new Solution("Nhạc Lofi Chill (Giảm áp lực, tăng tập trung)", "https://www.youtube.com/watch?v=jfKfPfyJRdk", SolutionType.MUSIC));
                list.add(new Solution("Bài tập giãn cơ cổ & vai gáy tại chỗ (10 Phút)", "https://www.youtube.com/watch?v=s-7lyv_EPFE", SolutionType.EXERCISE));
                list.add(new Solution("Phương pháp Pomodoro (Quản lý thời gian học/làm)", "https://tomato-timer.com/", SolutionType.READING));
                list.add(new Solution("Sách nói: Sức mạnh của thói quen (Tóm tắt)", "https://www.youtube.com/watch?v=Gk74y6wB5J4", SolutionType.MUSIC));
                break;

            case LOW:
            case NONE:
            default:
                list.add(new Solution("Playlist: Bắt đầu ngày mới tràn năng lượng", "https://www.youtube.com/watch?v=cQ6BvaE-0OQ", SolutionType.MUSIC));
                list.add(new Solution("Ebook: Đắc Nhân Tâm (Phát triển bản thân)", "https://sachvui.cc/dac-nhan-tam.html", SolutionType.READING));
                list.add(new Solution("Bài tập Cardio nhẹ nhàng buổi sáng (15 Phút)", "https://www.youtube.com/watch?v=O13uG_1hH2E", SolutionType.EXERCISE));
                list.add(new Solution("Podcast: Chữa Lành và Phát Triển (Đắp chăn nằm nghe)", "https://www.youtube.com/watch?v=O_14w9k1pII", SolutionType.MUSIC));
                break;
        }
        return list;
    }
}