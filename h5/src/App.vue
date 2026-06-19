<script setup lang="ts">
import { onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { showToast } from 'vant'

const router = useRouter()

onMounted(() => {
  console.log('App mounted')
})
</script>

<template>
  <router-view />
</template>

<style>
#app {
  width: 100%;
  max-width: 100%;
}
</style>
