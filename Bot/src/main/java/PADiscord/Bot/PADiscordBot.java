package PADiscord.Bot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
//import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import javax.security.auth.login.LoginException;

import java.awt.Color;

import java.text.MessageFormat;

//import java.util.concurrent.Executors;
//import java.util.concurrent.ScheduledExecutorService;
//import java.util.concurrent.TimeUnit;
//import java.util.Calendar;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
 
class BotInfo {
	private String version;
	private String developer;
	private String update;
 
	public void setVersion(String version) {
		this.version=version;
	}
	public String getVersion() {
		return version;
	}
	public void setDeveloper(String developer) {
		this.developer=developer;
	}
	public String getDeveloper() {
		return developer;
	}	
	public void setUpdate(String update) {
		this.update=update;
	}
	public String getUpdate() {
		return update;
	}
	
	public void printBotInfo(SlashCommandInteractionEvent event) {
		EmbedBuilder eb = new EmbedBuilder();

		String description = "僕はひりあ？さんのために作られたんだ～！";
		String versioninfo = MessageFormat.format(
				"\n僕のバージョンは[v{0}]だよ！\n僕の開発者は{1}さんだよ！\n最後に開発者さんにあったのは{2}だよ！",
				this.getVersion(),
				this.getDeveloper(),
				this.getUpdate()
				);
		eb.addField(description, versioninfo, false);
		eb.setColor(Color.PINK);

		event.replyEmbeds(eb.build()).setEphemeral(true).queue();
	}
	
	public void printTrainingInfo(SlashCommandInteractionEvent event) {
		EmbedBuilder eb =new EmbedBuilder();

		String description=MessageFormat.format("[v{0}]\n 開発者さんが僕に命を与えてくれたんだけど、その時に調教っていうことをされたんだ～。"
													+ "\nこの調教は恥ずかしいことって聞いたけどなんだかよくわかっていないんだよね...",this.getVersion());
			eb.addField("この前こんなことをされたんだ...///",description,false);
			eb.setColor(Color.PINK);
			event.replyEmbeds(eb.build()).setEphemeral(true).queue();
	}
	
     public void printHelp(SlashCommandInteractionEvent event,ArrayList<String> cmdname,Map<String,String> cmdinfo) {
         EmbedBuilder eb=new EmbedBuilder();
             eb.setTitle("僕はこんなことができるよ！");
             for (String name : cmdname) {
                 String command="/"+name;
                 String description=cmdinfo.getOrDefault(name, "ちょっと恥ずかしくて言えないな...///");
                 eb.addField(command,description,false);
             }
             eb.setColor(Color.PINK);
         event.replyEmbeds(eb.build()).setEphemeral(true).queue();
     }
    
	}


	public class PADiscordBot extends ListenerAdapter {
		private static JDA jda = null;
		private static final String BOT_TOKEN = System.getenv("DISCORD_BOT_TOKEN");
		//private static final String BOT_TOKEN = "";
		private static final BotInfo botinfo = new BotInfo();
		private static final ArrayList<String> cmdname=new ArrayList<>();
		
		//cmdname,cmdinfo
		private static final Map<String,String> cmdinfo=new HashMap<>();
		
		//userid,index
		private final Map<String,Integer> hentaiMap=new HashMap<>();
		private final Map<String,Boolean> nadeMap=new HashMap<>();

		public static void main(String[] args) throws LoginException{
			
			//BotInfo
			botinfo.setVersion("1.0.0");
			botinfo.setDeveloper("hrssss");
			botinfo.setUpdate("YYYY年MM月DD日");
  
			//CommandArray
			cmdname.add("info");cmdname.add("help");cmdname.add("traininginfo");cmdname.add("osawari");cmdname.add("mesugaki");
        
			cmdinfo.put(cmdname.get(0),"僕が自己紹介をするよ～！");cmdinfo.put(cmdname.get(1),"僕ができることを教えるよ～！");
			cmdinfo.put(cmdname.get(2),"ちょっと恥ずかしいけど、開発者さんからの調教内容を教えるよ～！");cmdinfo.put(cmdname.get(3),"僕のことを少しだけ触らせてあげるよ～！");
			cmdinfo.put(cmdname.get(4),"これはへんたいさんが喜ぶことって教えてもらったんだけど、なんでなんだろ？");
        
        
			jda = JDABuilder.createDefault(BOT_TOKEN)
					.setRawEventsEnabled(true)
					.enableIntents(GatewayIntent.MESSAGE_CONTENT)
					.addEventListeners(new PADiscordBot())
					.setActivity(Activity.playing("SM"))
					.build();
			jda.updateCommands().queue();
    
			for(int i=0;i<cmdinfo.size();i++) {
				jda.upsertCommand(cmdname.get(i),cmdinfo.get(cmdname.get(i))).queue();
			}
		}

		//forMessage
		@Override
		public void onMessageReceived(MessageReceivedEvent event) {
			if (event.getAuthor().isBot()) return;
            String inmsg=event.getMessage().getContentRaw();
            String outmsg="";
            
            if(inmsg.contains("なでて")||inmsg.contains("撫でて")||inmsg.contains("なでして")||inmsg.contains("撫でして")) {
            	outmsg="なでなでしてあげるよ～！よーしよーし";
            }
            
            if(inmsg.contains("ほめて")||inmsg.contains("褒めて")) {
            	outmsg="褒めてあげるよ～！よしよし、えらい！";
            }
            if(inmsg.contains("ぼっとちゃん")||inmsg.contains("ボットちゃん")) {
            	outmsg="僕のことよんだ～？";
            }
            
            if(!outmsg.isEmpty()) event.getChannel().sendMessage(outmsg).queue();
        }


    
		//forSlashCommand
		@Override
		public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
			String cmd=event.getName();//ReceiveCommand
			String outmsg="";
			
			switch(cmd) {
			
             	//infoコマンド
             	case "info" -> {
             		botinfo.printBotInfo(event);
             	}
             	
             	//traininginfoコマンド
             	case "traininginfo" -> {
             		botinfo.printTrainingInfo(event);
             	}
             	
                //helpコマンド
                case "help" -> {
                    botinfo.printHelp(event,cmdname,cmdinfo);
                }
                
                //osawariコマンド
                case "osawari" -> {
                	outmsg="少しなら触っていいよ...少しだけだからね!！///";
                	event.reply(outmsg).setEphemeral(true).addActionRow(
            				Button.primary("head", "頭を触る"),
            				Button.danger("breast","胸部を触る"),
            				Button.	danger("low-abdomen","下腹部を触る"),
            				Button.secondary("cease","やめる")
            		).queue();
                }
                
                //mesugakiコマンド
                case "mesugaki" -> {
                	outmsg="ざーこ♥ざーこ♥.........これでいいの？\n君ってへんたいさんなんだね！";
                	 event.reply(outmsg).setEphemeral(true).queue();
                }
                
                default -> event.reply("それはちょっとよくわからないかも...\n").setEphemeral(true).queue();
         }
     }
    
      //forButton
      @Override
      public void onButtonInteraction(ButtonInteractionEvent event) {
	    	//TextChannel channel = jda.getTextChannelById("channelid");
	    	//TextChannel channel = jda.getTextChannelById("1382708384221888562"); //THIS ID IS FOR TEST SERVER
	        //if (channel == null) return;
	        
	        String userId=event.getUser().getId();
	        int hentai=hentaiMap.getOrDefault(userId,0);
	        boolean nade=nadeMap.getOrDefault(userId,false);
	        
	        String outmsg="";
	        
	        switch (event.getComponentId()) {
          		case "head" -> {
          			if(hentai==0) {
          				int i=(int)(Math.random()*3);
          				outmsg=(i==1)?"えへへ...///":"もっとなでて～♥";
          			} else outmsg="...///";
          			nade=true;
          			nadeMap.put(userId,nade);
          			event.reply(outmsg).setEphemeral(true).queue();
          		}
          		
          		case "breast","low-abdomen" -> {
          			if(hentai==0) {
          				outmsg="へ、変態!!!";
          			} else if(hentai==1) {
          				outmsg="うぅ...触らないでよぉ...///";
          			} else if(hentai>1&&hentai<5) {
          				outmsg="......///";
          			} else if(hentai>=5) {
          				int i=(int)(Math.random()*3);
          				outmsg=(i==1)?"...ｯｯｯ...んっ///":"はぁ♥...んっ...やめてよぉ...///";
          			}
          			hentai++;
          			hentaiMap.put(userId,hentai);
          			event.reply(outmsg).setEphemeral(true).queue();
          		}
          		
          		case "cease" -> {
                  if(hentai!=0&&hentai<5){
              		event.reply("...ッ...バカ！もう触らせてあげないから！").setEphemeral(true).queue();
              		hentai=0;
                  }else if(hentai>=5) {
                	  event.reply("チャット欄の奥へ走り去ってしまった。").setEphemeral(true).queue();
                	  hentai=0;
                  }else if(hentai==0&&nade==true) {
                	  event.reply("もっとなでなでしてくれたっていいんだよ...?///").setEphemeral(true).queue();
                  }else if(hentai==0&&nade==false)event.reply("なにもしないの...？").setEphemeral(true).queue();
                  hentaiMap.put(userId,0);
                  nadeMap.put(userId,false);
          		}
	        }
	    }
      }