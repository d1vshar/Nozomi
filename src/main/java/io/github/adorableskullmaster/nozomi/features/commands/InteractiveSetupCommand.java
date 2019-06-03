package io.github.adorableskullmaster.nozomi.features.commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import io.github.adorableskullmaster.nozomi.core.util.CommandResponseHandler;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class InteractiveSetupCommand extends Command {

  private EventWaiter waiter;
  private HashMap<String, String> settingsMap;

  public InteractiveSetupCommand(EventWaiter waiter) {
    this.name = "setup";
    this.help = "This is an interactive setup that needs to be run every time bot configuration needs to be changed.";
    this.arguments = "++setup";
    this.userPermissions = new Permission[]{Permission.ADMINISTRATOR};
    this.guildOnly = true;
    this.waiter = waiter;
    this.settingsMap = new HashMap<>();
  }


  @Override
  protected void execute(CommandEvent commandEvent) {
    /*commandEvent.async(
        () -> commandEvent.getTextChannel().sendMessage("**Welcome to Nozomi Interactive Setup.** Let's start the setup.").queue(
            c -> main(commandEvent)
        )
    );*/
  }
  /*
  private void echo(CommandEvent commandEvent) {
    if (mapToSetup(commandEvent))
      commandEvent.reply(":white_check_mark: Bot setup successful.");
    else
      commandEvent.reply(":x: Something went wrong.");
  }

  private void joinText(CommandEvent commandEvent) {
    commandEvent.getTextChannel().sendMessage(":large_orange_diamond: Do you want to enable server join Messages? (yes/no)").queue(
        (s) -> waitForEvent(
            commandEvent,
            x -> {
              if (x.getMessage().getContentDisplay().equalsIgnoreCase("yes")) {
                commandEvent.getTextChannel().sendMessage("What should I say in the join embed? (text)").queue(
                    (c) -> waitForEvent(commandEvent, d -> {
                      settingsMap.put("join", d.getMessage().getContentDisplay());
                      commandEvent.getTextChannel().sendMessage("Do you want to include images/gif in server join messages? (yes/no)").queue(
                          (q) -> waitForEvent(
                              commandEvent,
                              w -> {
                                if (w.getMessage().getContentDisplay().equalsIgnoreCase("yes")) {
                                  commandEvent.getTextChannel().sendMessage("Enter url of the image/gif? (text)").queue(
                                      (o) -> waitForEvent(commandEvent, l -> {
                                        settingsMap.put("joinImg", l.getMessage().getContentDisplay());
                                        echo(commandEvent);
                                      })
                                  );
                                } else
                                  echo(commandEvent);
                              }
                          )
                      );
                    })
                );
              } else
                echo(commandEvent);
            }
        )
    );
  }

  private void applicant(CommandEvent commandEvent) {
    commandEvent.getTextChannel().sendMessage(":large_orange_diamond: Do you want to enable Applicant Notifier? (yes/no)").queue(
        (s) -> waitForEvent(
            commandEvent,
            x -> {
              if (x.getMessage().getContentDisplay().equalsIgnoreCase("yes")) {
                settingsMap.put("applicant", "yes");
              }
              joinText(commandEvent);
            }
        )
    );
  }

  private void bank(CommandEvent commandEvent) {
    commandEvent.getTextChannel().sendMessage(":large_orange_diamond: Do you want to enable Bank Notifier? (yes/no)").queue(
        (s) -> waitForEvent(
            commandEvent,
            x -> {
              if (x.getMessage().getContentDisplay().equalsIgnoreCase("yes")) {
                settingsMap.put("bank", "yes");
              }
              applicant(commandEvent);
            }
        )
    );
  }

  private void aatrack(CommandEvent commandEvent) {
    commandEvent.getTextChannel().sendMessage(":large_orange_diamond: Do you want to enable nation alliance tracker? (yes/no)").queue(
        (s) -> waitForEvent(
            commandEvent,
            x -> {
              if (x.getMessage().getContentDisplay().equalsIgnoreCase("yes")) {
                commandEvent.getTextChannel().sendMessage("What's your alliance tracker channel? (mention)").queue(
                    (c) -> waitForEvent(commandEvent, d -> {
                      settingsMap.put("aatrack", d.getMessage().getMentionedChannels().get(0).getId());
                      bank(commandEvent);
                    })
                );
              } else
                bank(commandEvent);
            }
        )
    );
  }

  private void vmb(CommandEvent commandEvent) {
    commandEvent.getTextChannel().sendMessage(":large_orange_diamond: Do you want to enable vm/beige notifier? (yes/no)").queue(
        (s) -> waitForEvent(
            commandEvent,
            (x) -> {
              if (x.getMessage().getContentDisplay().equalsIgnoreCase("yes")) {
                commandEvent.getTextChannel().sendMessage("What's your VM/Beige tracker channel? (mention)").queue(
                    (c) -> waitForEvent(commandEvent, d -> {
                      settingsMap.put("vmb", d.getMessage().getMentionedChannels().get(0).getId());
                      commandEvent.getTextChannel().sendMessage("Enter score filter (number)").queue(
                          (q)-> waitForEvent(commandEvent, z-> {
                            settingsMap.put("scorefilter",z.getMessage().getContentDisplay());
                            aatrack(commandEvent);
                          })
                      );
                    })
                );
              }
              else
                aatrack(commandEvent);
            }
        )
    );
  }

  private void warNotif(CommandEvent commandEvent) {
    commandEvent.getTextChannel().sendMessage(":large_orange_diamond: Do you want to enable new wars notifier? (yes/no)").queue(
        (s) -> waitForEvent(
            commandEvent,
            x -> {
              if (x.getMessage().getContentDisplay().equalsIgnoreCase("yes")) {
                commandEvent.getTextChannel().sendMessage("What's your offensive war channel? (mention)").queue(
                    (c) -> waitForEvent(commandEvent, d -> {
                      settingsMap.put("offwar", d.getMessage().getMentionedChannels().get(0).getId());
                      commandEvent.getTextChannel().sendMessage("What's your defensive war channel? (mention)").queue(
                          (m) -> waitForEvent(commandEvent, n -> {
                            settingsMap.put("defwar", n.getMessage().getMentionedChannels().get(0).getId());
                            vmb(commandEvent);
                          })
                      );
                    })
                );
              } else
                vmb(commandEvent);
            }
        )
    );
  }

  private void key(CommandEvent commandEvent) {
    commandEvent.getTextChannel().sendMessage(":large_orange_diamond: What's your Politics And War gov api key? **API KEY WILL BE DELETED**.").queue(
        (c) -> waitForEvent(commandEvent, x -> {
          settingsMap.put("key", x.getMessage().getContentDisplay());
          x.getMessage().delete().queue();
          warNotif(commandEvent);
        })
    );
  }

  private void aaid(CommandEvent commandEvent) {
    commandEvent.getTextChannel().sendMessage(":large_orange_diamond: What's your Politics And War alliance ID? (id)").queue(
        (c) -> waitForEvent(commandEvent, x -> {
          settingsMap.put("id", x.getMessage().getContentDisplay());
          key(commandEvent);
        })
    );
  }

  private void member(CommandEvent commandEvent) {
    commandEvent.getTextChannel().sendMessage(":large_orange_diamond: What's your main member role? (id)").queue(
        (c) -> waitForEvent(commandEvent, x -> {
          settingsMap.put("member", x.getMessage().getContentDisplay());
          aaid(commandEvent);
        })
    );
  }

  private void gov(CommandEvent commandEvent) {
    commandEvent.getTextChannel().sendMessage(":large_orange_diamond: What's your gov channel? (mention)").queue(
        (c) -> waitForEvent(commandEvent, x -> {
          settingsMap.put("gov", x.getMessage().getMentionedChannels().get(0).getId());
          member(commandEvent);
        })
    );
  }

  private void log(CommandEvent commandEvent) {
    commandEvent.getTextChannel().sendMessage(":large_orange_diamond: What's your bot-log channel? (mention)").queue(
        (c) -> waitForEvent(commandEvent, x -> {
          settingsMap.put("log", x.getMessage().getMentionedChannels().get(0).getId());
          gov(commandEvent);
        })
    );
  }

  private void main(CommandEvent commandEvent) {
    commandEvent.getTextChannel().sendMessage(":large_orange_diamond: What's your main server channel? (mention)").queue(
        (c) -> waitForEvent(commandEvent, x -> {
          settingsMap.put("main", x.getMessage().getMentionedChannels().get(0).getId());
          log(commandEvent);
        })
    );
  }

  private boolean mapToSetup(CommandEvent commandEvent) {
    try (Connection connection = Instances.getConnection()) {
      DSLContext db = DSL.using(connection);

      //GUILD
      int pwid = Integer.parseInt(settingsMap.get("id"));
      String pwkey = settingsMap.get("key");
      boolean isWarNotifier = settingsMap.containsKey("offwar") && settingsMap.containsKey("defwar");
      boolean isNationTracker = settingsMap.containsKey("aatrack");
      boolean isBank = settingsMap.containsKey("bank");
      boolean isVMBeige = settingsMap.containsKey("vmb");
      int scoreFilter = Integer.parseInt(settingsMap.get("scorefilter"));
      boolean applicant = settingsMap.containsKey("applicant");
      boolean isJoin = settingsMap.containsKey("join");
      long memberrole = Long.parseLong(settingsMap.get("member"));

      long id = commandEvent.getGuild().getIdLong();

      //TEXTS
      if(isJoin) {
        String join = settingsMap.get("join");
        UpdateSetMoreStep<TextsRecord> texts = db.update(TEXTS)
            .set(TEXTS.JOIN, join);
        if(settingsMap.containsKey("joinImg")) {
          String joinImg = settingsMap.get("joinImg");
          texts.set(TEXTS.JOINIMG, joinImg);
        }
        texts.where(TEXTS.ID.eq(id)).execute();
      }

      //CHANNELS
      long mainchannel = Long.parseLong(settingsMap.get("main"));
      long govchannel = Long.parseLong(settingsMap.get("gov"));
      long logchannel = Long.parseLong(settingsMap.get("log"));

      UpdateSetMoreStep<ChannelsRecord> chanels = db.update(CHANNELS)
          .set(CHANNELS.MAINCHANNEL, mainchannel)
          .set(CHANNELS.GOVCHANNEL, govchannel)
          .set(CHANNELS.LOGCHANNEL, logchannel);

      if(isWarNotifier) {
        chanels.set(CHANNELS.OFFENSIVECHANNEL,Long.parseLong(settingsMap.get("offwar")));
        chanels.set(CHANNELS.DEFENSIVECHANNEL,Long.parseLong(settingsMap.get("defwar")));
      }
      if(isVMBeige) {
        chanels.set(CHANNELS.VMBEIGECHANNEL,Long.parseLong(settingsMap.get("vmb")));
      }
      if(isNationTracker) {
        chanels.set(CHANNELS.NATIONTRACKERCHANNEL,Long.parseLong(settingsMap.get("aatrack")));
      }
      chanels.where(CHANNELS.ID.eq(id)).execute();

      db.update(GUILDS)
          .set(GUILDS.PWID, pwid)
          .set(GUILDS.PWKEY, pwkey)
          .set(GUILDS.WARNOTIFIER, isWarNotifier)
          .set(GUILDS.NATIONTRACKER, isNationTracker)
          .set(GUILDS.BANKNOTIFIER, isBank)
          .set(GUILDS.VMBEIGETRACKER, isVMBeige)
          .set(GUILDS.NATIONSCOREFILTER, scoreFilter)
          .set(GUILDS.AUTOCOUNTER, true)
          .set(GUILDS.APPLICANTNOTIFIER, applicant)
          .set(GUILDS.JOINTEXTS, isJoin)
          .set(GUILDS.LEAVETEXTS, false)
          .set(GUILDS.MEMBERROLE, memberrole)
          .set(GUILDS.SETUP, true)
          .where(GUILDS.ID.eq(id))
          .execute();

      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }*/

  private void waitForEvent(CommandEvent commandEvent, Consumer<MessageReceivedEvent> action) {
    waiter.waitForEvent(
        MessageReceivedEvent.class,
        (event) -> event.getAuthor().equals(commandEvent.getAuthor()) && event.getChannel().equals(commandEvent.getChannel()),
        action,
        120,
        TimeUnit.SECONDS,
        () -> CommandResponseHandler.timeout(commandEvent)
    );
  }
}
