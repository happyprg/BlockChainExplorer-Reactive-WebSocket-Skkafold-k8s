#!/bin/sh

log() {
  echo "[wait-for] [`date +'%Y%m%d%H%M%S'`] $@"
}

wait_for() {
  host=$1 && port=$2
  log "wait '$host':'$port'"

  while ! nc -z "$host" "$port" > /dev/null 2>&1 ; do
    log "wait attempt '${host}:${port}' [$i]"
    sleep 5
  done

  log "wait finish '$host:$port' [`expr $(date +%s) - $START`] seconds"
  exit 0
}

START=$(date +%s)
pids=""
for i in $@ ; do
  wait_for "${i%%:*}" "${i##*:}" &
  pids="$pids $!"
done

status=0
for pid in $pids; do
  if ! wait $pid ; then
    status=1
  fi
done

log "wait done with status=$status"
exit $status
