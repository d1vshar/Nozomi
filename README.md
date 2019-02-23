# Nozomi

Nozomi is a Discord bot made by WarGamer for the online browser game Politics and War.

## Features

- Nation and Alliance search
- Finds best counters for your raiders
- New War Monitor
- Analyze a target

#### Planned Features

- New Applicant Monitor
- Trade Monitor
- MMR Audit command

## How to use?

The bot with the current features works very well. 

Slight problem: **You need to configure it and host it on your own.**

I run my own instance of the bot on 500mb RAM server. 
I highly recommend Heroku for hosting the bot. It's free for 500mb (needs your credit card tho... pretty safe).

### Self-Hosting

#### Prerequisites

- Postgres Database
- Maven 3
- JDK 8

#### Configuring the bot

You have to configure the bot by adding `config.json` file in `src\main\resources\config\config.json`.
You will have to create the folder since it does not exist.

As for the structure of the `config.json` and more instructions have a look at the wiki of the project.

#### Building the bot

Clone the repo. Open command line in the folder. Run `maven compile assembly:single`. A jar will be made in `target` folder.

#### Running the bot 

Running the generated executable jar will run the bot. Make sure the postgres db server is already up before running it. 

### Self-Hosting for free 24/7 on Heroku (the way I host it)

Make a Heroku app and provide Heroku Postgres add-on and use the db url there in `config.json` as mentioned in wiki.

Build the app mentioned in normal self hosting... and then type this in terminal (in same folder)

`heroku deploy:jar target\Nozomi-jar-with-dependencies.jar -a <YOUR_HEROKU_APP_NAME>`

Then use the Heroku dashboard to scale and that's it.

## Contributing

Fork. Make a branch. Make changes. Start PR.

**Author:** Adorable-SkullMaster