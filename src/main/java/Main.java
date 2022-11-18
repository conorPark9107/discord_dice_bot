import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.security.auth.login.LoginException;

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;

public class Main extends ListenerAdapter
{

    private ArrayList<DiceMember> list = new ArrayList<>();


    public static void main(String[] args) throws LoginException
    {

        String discord_token = new Token().getToken();
        JDABuilder.createLight(discord_token,
                        GatewayIntent.GUILD_MESSAGES,
                        GatewayIntent.DIRECT_MESSAGES)
                .addEventListeners(new Main())
                .setActivity(Activity.playing("!도움말"))
                .build();

    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event)
    {

        if(!event.getAuthor().isBot() && event.getChannel().getName().contains("다이스")) {

            String[] line = event.getMessage().getContentRaw().split(" ");

            // 도움말.
            if(line[0].equals("!도움말")) {
                event.getChannel().sendMessage(showHelp()).queue();
            }

            // 주사위 굴리기.
            if(line[0].equals("-")) {
                List<Member> mentionlist = event.getMessage().getMentionedMembers();
                if(mentionlist.size() > 0) {
                    for(int i = 0; i < mentionlist.size(); i++) {
                        int num = getRandomNumber();
                        list.add(new DiceMember(mentionlist.get(i).getAsMention(), num));
                        event.getChannel().sendMessage(mentionlist.get(i).getAsMention() + " : " + num).queue();
                    }

                }else {
                    int num = getRandomNumber();
                    list.add(new DiceMember(event.getAuthor().getAsMention(), num));
                    event.getChannel().sendMessage(event.getAuthor().getAsMention() + " : " + num).queue();
                }

                // 결과 확인.
            }else if(line[0].equals("=")) {

                if(list.size() == 0) {
                    event.getChannel().sendMessage("아무도 다이스 안돌렸음!!").queue();
                    return;
                }

                Collections.sort(list);
                String sb = showRandomList(list);
                event.getChannel().sendMessage(sb).queue();

                // 다이스 리셋.
            }else if(line[0].equals("---")) {
                list.clear();
                event.getChannel().sendMessage("리셋 했음!").queue();

            }
        }
    }

    private String showHelp() {
        String msg = "**```"
                + "주사위 굴리기  👉 - \n"
                + "리셋 하기      👉 --- ( -를 3개 붙이면 됨) \n"
                + "대신 돌려주기  👉 - @닉네임 \n"
                + "--------------------------------------------------- \n"
                + "대신돌리기는 계속해서 mension을 이어나가도 됨 \n"
                + "ex) - @metzzi @setzzi @netzzi @dutzzi \n"
                + "```**" + "\n";
        return msg;
    }

    // 다이스 결과물을 만드는 함수
    private String showRandomList(ArrayList<DiceMember> list) {
        StringBuilder sb = new StringBuilder();
        sb.append("**`  🎲  다이스 결과  🎲   `**" + "\n");
        for(int i = 0; i < list.size(); i++) {

            switch (i) {
                case 0: sb.append("1️⃣ "); break;
                case 1: sb.append("2️⃣ "); break;
                case 2: sb.append("3️⃣ "); break;
                case 3: sb.append("4️⃣ "); break;
                case 4: sb.append("5️⃣ "); break;
                case 5: sb.append("6️⃣ "); break;
                case 6: sb.append("7️⃣ "); break;
                case 7: sb.append("8️⃣ "); break;
                case 8: sb.append("9️⃣ "); break;
                case 9: sb.append("🔟 "); break;
                default : sb.append("😿 "); break;
            }
            DiceMember d = list.get(i);

            sb.append(d.getName()).append("  ").append(d.getNum()).append("\n");
        }

        return sb.toString();
    }

    // 랜덤으로 값 추출하는 함수.
    private int getRandomNumber() {
        int num = (int) (Math.random() * 101);
        return num;
    }
}