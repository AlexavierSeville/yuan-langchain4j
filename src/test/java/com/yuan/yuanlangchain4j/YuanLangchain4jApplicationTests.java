package com.yuan.yuanlangchain4j;

import com.yuan.yuanlangchain4j.assistant.Assistant;
import com.yuan.yuanlangchain4j.assistant.MemoryChatAssistant;
import com.yuan.yuanlangchain4j.assistant.SeparateChatAssistant;
import com.yuan.yuanlangchain4j.bean.ChatMessages;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.Arrays;

@SpringBootTest
class YuanLangchain4jApplicationTests {

    // 测试.txt DeepSeek sdk是否能够正常使用
    @Test
    void testDeepSeekDemo() {
        OpenAiChatModel model = OpenAiChatModel.builder()
                .baseUrl("https://api.deepseek.com/v1")
                .apiKey("sk-你的SDK")
                .modelName("deepseek-chat")
                .build();

        String answer = model.chat("hi,你是谁？");
        System.out.println(answer);
        // 回复：你好呀！😊 我是DeepSeek Chat，由深度求索公司创造的智能AI助手。
        // 我的使命是帮助你解答问题、提供信息、陪你聊天，或者协助完成各种任务！
        // 无论是学习、工作，还是日常生活中的小困惑，都可以找我聊聊~ ✨
    }

    //测试langchain4j与springboot集成
    @Resource
    private OpenAiChatModel openAiChatModel;

    @Test
    public void testSpringLangChain() {
        String chat = openAiChatModel.chat("你知道是谁在调用你么？");
        System.out.println(chat);
    }

    //测试通过assistant方式实现
    @Test
    public void aiServiceTest() {
        Assistant assistant = AiServices.create(Assistant.class, openAiChatModel);
        String answer = assistant.chat("你哪位？");
        System.out.println(answer);

    }

    //注解方式实现(以上方法都有缺点，就是都没有记忆)
    @Resource
    private Assistant assistant;

    @Test
    public void aiServiceAnnotationTest() {
        String answer = assistant.chat("RAG是什么？");
        System.out.println(answer);

    }

    // 简单实现有记忆对话，但是很low
    @Test
    public void aiServiceMemoryTest() {
        //第一句话
        UserMessage userMessage1 = UserMessage.userMessage("我是元仔，记住我");
        ChatResponse chat1 = openAiChatModel.chat(userMessage1);
        AiMessage aiMessage1 = chat1.aiMessage();
        System.out.println(aiMessage1.text());

        //第二句话
        UserMessage userMessage2 = UserMessage.userMessage("你还记得我是谁吗？");
        ChatResponse chat2 = openAiChatModel.chat(Arrays.asList(userMessage1, aiMessage1, userMessage2));
        AiMessage aiMessage2 = chat2.aiMessage();
        System.out.println(aiMessage2.text());
        // 好的，元仔！我已经记住你啦～下次聊天随时喊我“元仔”就能快速认出你哦！有什么想聊的或者需要帮忙的，随时告诉我～ 😊
        // 当然记得！你是**元仔**呀～（闪闪发光✨的记忆力上线中）
        // 下次直接喊“元仔专属问题！”我可能会秒回哦～ 今天想聊点什么？ 😄
    }

    // 可以在创建assistant时就赋予记忆能力
    @Test
    public void aiChatMemoryTest() {
        //设置可以接收多少个聊天记忆
        MessageWindowChatMemory chatMemory = MessageWindowChatMemory.withMaxMessages(10);
        Assistant assistant = AiServices
                .builder(Assistant.class)
                .chatLanguageModel(openAiChatModel)
                .chatMemory(chatMemory)
                .build();
        String answer1 = assistant.chat("我是元仔");
        System.out.println(answer1);

        String answer2 = assistant.chat("你还记得老子我是谁么？");
        System.out.println(answer2);

        //你好呀！😊 我是DeepSeek Chat，可以叫我DeepSeek或者小深~ 元仔是你的名字吗？听起来很可爱呢！
        // ✨ 有什么我可以帮你的吗？无论是学习、生活还是娱乐，我都会尽力帮你解答哦！💡
        // 哈哈，当然记得啦！你可是自称“老子”的元仔嘛～😎 不过我这记性嘛……（挠头）每次聊天都是崭新的开始，
        // 但只要你一提，我立马就能接上你的节奏！所以，今天有什么好玩的事儿要一起聊，还是需要帮忙解决点啥？随时等你吩咐～ ✨
    }

    // 这是注解实现的
    @Resource
    private MemoryChatAssistant memoryChatAssistant;

    @Test
    public void aiChatMemoryAnnotationTest() {
        String answer1 = memoryChatAssistant.chat("我是元仔");
        System.out.println(answer1);

        String answer2 = memoryChatAssistant.chat("你还记得我的名字么？");
        System.out.println(answer2);

        // 你好呀！😊 我是DeepSeek Chat，可以叫我小深或者DeepSeek~ 元仔是你的名字吗？听起来很可爱呢！
        // ✨ 有什么我可以帮你的吗？无论是聊天、解答问题，还是分享趣事，我都超乐意陪你哦！💬🚀

        // 当然记得啦！你刚刚说过你叫**元仔**嘛~ 😊 这么可爱的名字，我怎么会忘记呢？
        // 有什么想聊的或者需要帮忙的，随时告诉我哦！✨💬

    }

    // 实现不同用户的对话隔离
    @Resource
    private SeparateChatAssistant separateChatAssistant;

    @Test
    public void aiSeparateChatMemoryAnnotationTest() {
        String answer1 = separateChatAssistant.chat(1,"我是元仔");
        System.out.println(answer1);

        String answer2 = separateChatAssistant.chat(1,"你还记得我叫什么？");
        System.out.println(answer2);

        String answer3 = separateChatAssistant.chat(2,"你知道我是谁么？");
        System.out.println(answer3);

        //你好呀！😊 我是DeepSeek Chat，可以叫我DeepSeek或者小深~ 很高兴认识你！元仔是你的昵称吗？有什么我可以帮你的吗？✨
        // 当然记得啦！你刚刚告诉我你叫**元仔**嘛～😊 我会认真记住每个用户的昵称，所以不用担心我会忘记哦！有什么想聊的或者需要帮忙的，随时告诉我～✨
        //
        // 目前，我无法直接识别你的身份，因为我们之间的对话是匿名的。我不会记录或存储你的个人信息，除非你在对话中主动提供相关信息（比如名字、账号等）。
        // 不过，如果你之前曾主动提及过某些个人细节（例如兴趣爱好、所在地等），我可能会在当前的对话上下文中记住这些信息，但一旦对话结束，这些信息不会被保留。
        // 如果你想让我用某个特定的方式称呼你，可以随时告诉我！ 😊
    }

    //MongoDB实现持久化
    @Resource
    private MongoTemplate mongoTemplate;

    //测试插入
    @Test
    public void testInsert() {
        ChatMessages chatMessages = new ChatMessages();
        chatMessages.setContent("元仔的聊天记录");
        mongoTemplate.insert(chatMessages);
    }

    //测试查询
    @Test
    public void testFind() {
        ChatMessages chatMessages = mongoTemplate.findById("683e9b3e16f5c078d6e87743", ChatMessages.class);
        System.out.println(chatMessages);
    }

    //测试修改
    @Test
    public void testUpdate() {
        Criteria criteria = Criteria.where("_id").is("683e9b3e16f5c078d6e87743");
        Query query = new Query(criteria);
        Update update = new Update();
        update.set("content", "元仔的聊天记录update");
        // 这个方法id找不到就会新增
        mongoTemplate.upsert(query, update, ChatMessages.class);
    }

    //测试删除
    @Test
    public void testDelete() {
        Criteria criteria = Criteria.where("_id").is(1);
        Query query = new Query(criteria);
        // 删除时id找不到不会报错
        mongoTemplate.remove(query, ChatMessages.class);
    }

    //系统提示测试
    @Test
    public void promptTest(){
        String answer = separateChatAssistant.chat(5, "你是谁，今天是几号？");
        System.out.println(answer);
        // *手持法杖，白袍随风飘动，目光深邃地注视着对方*
        // 我是灰袍甘道夫，中土世界的守护者之一。年轻的旅人啊，你为何踏上这片土地？

        // *突然严肃地眯起眼睛，法杖重重敲击地面*
        // "元仔"？*低沉而威严的声音* 魔戒不是儿戏！它已经蛊惑了无数贪婪的心灵。告诉我，是谁指引你寻找至尊魔戒的？
    }

    @Test
    public void userMessageTest(){
        String answer1 = memoryChatAssistant.chat("我是元仔");
        System.out.println("=======对话分割线=======");
        System.out.println(answer1);
        System.out.println("=======对话分割线=======");

        String answer2 = memoryChatAssistant.chat("我37岁了");
        System.out.println("=======对话分割线=======");
        System.out.println(answer2);
        System.out.println("=======对话分割线=======");

        String answer3 = memoryChatAssistant.chat("你知道我谁吗？多大了？");
        System.out.println("=======对话分割线=======");
        System.out.println(answer3);
        System.out.println("=======对话分割线=======");

        // =======对话分割线=======
        // 哈哈，元仔！真係好開心見到你呀！😄 你最近點呀？過得開唔開心？💖 我哋真係好朋友嚟㗎，所以同你傾計特別有heart！❤️🔥
        //
        // 今日天氣咁好，有冇出去行下呀？🌞 定係喺屋企hea緊？🛋️ 記住要食多啲好嘢，照顧好自己啊！🍲🍉
        //
        // 有咩想同我分享？我隨時ready聽你講㗎！😊💬 你知㗎，我永遠撐你！💪✨
        //
        // #老友鬼鬼 #開心share #粵語mode全開 😝
        // =======对话分割线=======
        // =======对话分割线=======
        // 哈哈，元仔！37歲正當年啦！💪🔥 男人四十一枝花，你仲後生過我啦～😎✨
        //
        // 喂！你而家係咪事業有成、家庭幸福嗰種人生贏家mode呀？🏆💼 定係仲keep住後生仔嘅心態周圍去玩？🎮✈️
        //
        // 37歲最正啦！又夠成熟又夠活力，飲啤酒都仲可以劈多兩杯🍻🤣 記住啊，年齡只係數字，我睇你個心仲後生過啲後生仔！💖😝
        //
        // 最近有冇咩新搞作？同我分享下啦～🤗💬 等你請我飲茶㗎！🍵🥟
        //
        // #三十七正青春 #老友萬歲 #年齡只係浮雲 🎉😂
        // =======对话分割线=======
        // =======对话分割线=======
        // 哈哈，元仔！緊係知你係邊個啦～你係我嘅超級老友嚟㗎嘛！😍💖
        //
        // 雖然我冇記憶功能，但每次見返你個名「元仔」都覺得好親切！✨ 你之前話過你37歲㗎嘛～🎂（我記性幾好啩？😎）
        //
        // 不過最緊要係——無論你係17定70歲，你永遠都係我心目中嗰個活潑嘅元仔！👶➡️👨💼🔥
        //
        // 等陣先...你唔會係外星人假扮㗎嘛？👽🤣 講笑咋～快啲同我講下今日有咩趣事！💬🎉
        //
        // #老友記認證 #年齡係秘密但係你話我知㗎 #一齊癲過 🤪💥
        //
        // （P.S. 如果記錯你嘅資料要話我知呀～即刻改過！🛠️😘）
        // =======对话分割线=======
    }

    //测试V注解
    @Test
    public void aiVAnnotationTest() {

        String username="元仔";
        int age=10;

        String answer1 = separateChatAssistant.chat3(11,"来者何人，你知道我多大了么？你知道现在是什么时候么？",username,age);
        System.out.println(answer1);

        // *轻抚长须，露出慈祥而睿智的微笑*
        // 10岁...多么奇妙的年纪。*法杖轻点地面* 就像年轻的霍比特人一样，看似弱小却蕴含着改变世界的力量。现在是2025年6月4日，但对你而言，这将是命运开始转动的日子。*突然严肃* 元仔，你准备好接受这个重担了吗？
    }

    // 测试Function Calling
    @Test
    public void aiMathTest() {
        String answer1 = separateChatAssistant.chat(12,"1+2等与几，777777777的平方根是多少");
        System.out.println(answer1);

        // 微软计算器结果3，8,819.1709927
        // 运行结果：
        // 调用加法运算
        // 调用平方根运算
        // 1加2等于3，而77777777的平方根大约是8819.17。
    }

}
