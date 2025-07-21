<template>
  <div class="fa_container">
    <!-- 存放three.js的渲染效果 -->
    <div class="container" ref="container"></div>
    <div class="card" id="cardId">模型标签</div>
  </div>
</template>

<script>
import * as THREE from 'three'
import { OrbitControls } from 'three/examples/jsm/controls/OrbitControls'
import { DragControls } from 'three/examples/jsm/controls/DragControls.js'
// import { CSS2DRenderer, CSS2DObject } from 'three/examples/jsm/renderers/CSS2DRenderer'
import { CSS3DRenderer, CSS3DObject } from 'three/examples/jsm/renderers/CSS3DRenderer'
// import { GLTFLoader } from 'three/examples/jsm/loaders/GLTFLoader'

const scene = new THREE.Scene()
let renderer = new THREE.WebGLRenderer({
  antialias: true,
  alpha: true,
  logarithmicDepthBuffer: true
})
let cssRenderer = new CSS3DRenderer()
let camera, control, dControls
// let loader = new GLTFLoader()

export default {
  name: 'model',

  mounted() {
    this.init()
    this.render()
  },

  methods: {
    init() {
      this.dom = this.$refs['container']

      // 坐标轴+灯光
      const axes = new THREE.AxesHelper(50)
      const directLight = new THREE.AmbientLight(0x404040, 2)
      scene.add(axes, directLight)

      // 相机
      camera = new THREE.PerspectiveCamera(
        45,
        this.dom.offsetWidth / this.dom.offsetHeight,
        0.001,
        10000
      )
      camera.position.set(25, 25, 25)

      // 1、WebGLRenderer渲染器：用于渲染三维模型
      renderer.setSize(1902, 935)
      renderer.outputEncoding = THREE.sRGBEncoding
      this.dom.appendChild(renderer.domElement)

      // 2、把标签DOM转化为css2D元素，相当于在标签DOM外添加一个div
      this.compDiv = document.getElementById('cardId')
      this.compTag = new CSS3DObject(this.compDiv)
      this.compTag.scale.set(0.04, 0.04, 0.04)
      scene.add(this.compTag)

      // 3、css2D渲染器：用于渲染模型的2D或者3D标签
      cssRenderer.setSize(1902, 935)
      cssRenderer.domElement.style.position = 'absolute'
      cssRenderer.domElement.style.top = 0
      this.dom.appendChild(cssRenderer.domElement)

      // 创建一个球
      const spherGeometry = new THREE.SphereGeometry(5, 32, 16)
      const spherMaterial = new THREE.MeshLambertMaterial({ color: '#95B9F0' })
      const sphere = new THREE.Mesh(spherGeometry, spherMaterial)
      scene.add(sphere)

      // 控制器
      control = new OrbitControls(camera, cssRenderer.domElement)

      let dragObj = []
      dragObj.push(sphere)
      dControls = new DragControls(dragObj, camera, cssRenderer.domElement)
      dControls.addEventListener('dragstart', function () {
        control.enabled = false
      })
      dControls.addEventListener('dragend', function () {
        control.enabled = true
      })
    },

    render() {
      requestAnimationFrame(this.render.bind(this))
      control.update()
      cssRenderer.render(scene, camera)
      renderer.render(scene, camera)
    }
  }
}
</script>

<style>
.container {
  width: 1902px;
  height: 935px;
  overflow: hidden;
  background: black;
  display: inline-block;
}
.card {
  height: 200px;
  width: 200px;
  /* position: absolute; */
  display: inline-block;
  color: white;
  background: blueviolet;
  top: 8px;
}
</style>
