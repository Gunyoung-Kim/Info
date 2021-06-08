REPOSITORY=/home/ec2-user/info/git

cd $REPOSITORY/Info/

echo "> Git Pull"

git pull

echo "> Project Build"

./mvnw clean package

echo "> Copy Result JAR Files"

cp ./target/*.jar $REPOSITORY/

echo "> Check currently running applcication's PID"

CURRENT_PID=$(pgrep -f Info)

echo "$CURRENT_PID"

if [ -z $CURRENT_PID ]; then
    echo "> There is no such application which is currently running"
else
    echo "> kill -2 $CURRENT_PID"
    kill -2 $CURRENT_PID
    sleep 5
fi

echo "> Deploy new Application"

JAR_NAME=$(ls $REPOSITORY/ |grep 'Info' | tail -n 1)

echo "> JAR Name: $JAR_NAME"

nohup java -jar $REPOSITORY/$JAR_NAME & 


